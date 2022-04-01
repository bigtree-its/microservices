package com.bigtree.chef.orders.controller;

import com.bigtree.chef.orders.model.request.PaymentIntentRequest;
import com.bigtree.chef.orders.model.response.PaymentIntentResponse;
import com.bigtree.chef.orders.entity.Basket;
import com.bigtree.chef.orders.entity.BasketItem;
import com.bigtree.chef.orders.entity.Payment;
import com.bigtree.chef.orders.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
public class StripeCheckoutController {

    @Value("${stripe.private.key}")
    private String stripeKey;
    @Value("${stripe.success.url}")
    private String successUrl;
    @Value("${stripe.cancel.url}")
    private String cancelUrl;
    @Value("${stripe.currency}")
    private String currency;

    @Autowired
    PaymentService paymentService;

    @CrossOrigin(origins = "*")
    @PostMapping("/create-checkout-session")
    public ResponseEntity<PaymentIntentResponse> createCheckoutSession(@RequestBody Basket basket) {
        Stripe.apiKey = stripeKey;
        Set<BasketItem> items = basket.getItems();
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        for (BasketItem item : items) {
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(item.getQuantity().longValue())
                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(currency)
                            .setUnitAmount(item.getPrice().longValue() * 100)
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(item.getProductName()).build())
                            .build())
                    .build();
            lineItems.add(lineItem);
        }

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(successUrl)
                        .setCancelUrl(cancelUrl)
                        .addAllLineItem(lineItems)
                        .build();
        try {
            final Session session = Session.create(params);
            return ResponseEntity.ok(PaymentIntentResponse.builder().id(session.getId()).build());
        } catch (StripeException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(PaymentIntentResponse.builder().error(true).build());

    }

    @CrossOrigin(origins = "*")
    @PostMapping("/create-payment-intent")
    private ResponseEntity<PaymentIntentResponse> createPaymentIntent(@RequestBody PaymentIntentRequest request) {
        Stripe.apiKey = stripeKey;
        long totalCost = request.getSubTotal() + request.getDeliveryCost() + request.getPackagingCost() + request.getSaleTax();
        log.info("Received request create Payment Intent for amount {} pence.", totalCost);
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setCustomer(request.getCustomerEmail())
                        .setCurrency(currency)
                        .setAmount(totalCost)
                        // Verify your integration in this guide by including this parameter
                        .putMetadata("integration_check", "accept_a_payment")
                        .build();
        try {
            PaymentIntent intent = PaymentIntent.create(params);
            final PaymentIntentResponse response = PaymentIntentResponse.builder()
                    .id(intent.getId())
                    .amount(intent.getAmount())
                    .currency(intent.getCurrency())
                    .clientSecret(intent.getClientSecret())
                    .object(intent.getObject())
                    .paymentMethod(intent.getPaymentMethodTypes().get(0))
                    .status(intent.getStatus())
                    .liveMode(intent.getLivemode())
                    .metaData(intent.getMetadata())
                    .chargesUrl(intent.getCharges().getUrl())
                    .build();
            log.info("Returning payment intent for amount: {}, Id: {}", intent.getAmount(), intent.getId());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    createPayment(response, request);
                }
            }).start();
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            log.error("Unable to create payment intent {}", e.getMessage());
            PaymentIntentResponse response = PaymentIntentResponse.builder()
                    .error(true)
                    .errorMessage(e.getMessage())
                    .build();
            return ResponseEntity.ok(response);
        }

    }

    private void createPayment(PaymentIntentResponse response, PaymentIntentRequest request) {
        final Payment payment = Payment.builder()
                .paymentMethod(response.getPaymentMethod())
                .currency(response.getCurrency())
                .amount(BigDecimal.valueOf(response.getAmount()))
                .customerEmail(request.getCustomerEmail())
                .stripeReference(response.getId())
                .status(response.getStatus())
                .build();
        final Payment saved = paymentService.create(payment);
        log.info("PaymentIntent has been created for customer {} with Reference {}", payment.getCustomerEmail(), saved.getStripeReference());
    }

    private long getOrderAmount(Basket basket) {
        BigDecimal total = BigDecimal.ZERO;
        Set<BasketItem> items = basket.getItems();
        for (BasketItem item : items) {
            total = total.add(item.getTotal());
        }
        return total.longValue();
    }

}
