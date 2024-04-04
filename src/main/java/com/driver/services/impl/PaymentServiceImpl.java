package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception
    {
        //Attempt a payment of amountSent for reservationId using the given mode ("cASh", "card", or "upi")
        //If the amountSent is less than bill, throw "Insufficient Amount" exception, otherwise update payment attributes
        //If the mode contains a string other than "cash", "card", or "upi" (any character in uppercase or lowercase), throw "Payment mode not detected" exception.
        //Note that the reservationId always exists

        //get the reservation
        Reservation reservation=reservationRepository2.findById(reservationId).get();

        //calculate bill = NumberOfHours * PricePerHours
        Spot spot=reservation.getSpot();
        int bill=reservation.getNumberOfHours()*spot.getPricePerHour();

        //check if amountSent is less than bill or not
        if(amountSent<bill)
        {
            throw new Exception("Insufficient Amount");
        }



    //if payment mode is not matched
        if(!("cash".equals(mode.toLowerCase()) || "card".equals(mode.toLowerCase()) || "upi".equals(mode.toLowerCase())))
        {
            throw new Exception("Payment mode not detected");
        }


        //do the payment staffs
        Payment payment=new Payment();
        payment.setReservation(reservation);

        //add payment mode
        if("cash".equals(mode.toLowerCase()))
        {
            payment.setPaymentMode(PaymentMode.CASH);
        }
        else if("card".equals(mode.toLowerCase()))
        {
            payment.setPaymentMode(PaymentMode.CARD);
        }
        else
        {
            payment.setPaymentMode(PaymentMode.UPI);
        }

        //mark payment as completed
        payment.setPaymentCompleted(true);

        //save then payment into db
        paymentRepository2.save(payment);

        return payment;
    }
}
