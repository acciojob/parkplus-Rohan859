package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception
    {
        //Reserve a spot in the given parkingLot such that the total price is minimum. Note that the price per hour for each spot is different
        //Note that the vehicle can only be parked in a spot having a type equal to or larger than given vehicle
        //If parkingLot is not found, user is not found, or no spot is available, throw "Cannot make reservation" exception.

        //if user not found or parkingLot not found
//        User user=userRepository3.findById(userId).get();
//        ParkingLot parkingLot=parkingLotRepository3.findById(parkingLotId).get();
//
//        if(user==null || parkingLot==null)
//        {
//            throw new Exception("Cannot make reservation");
//        }
//
//
//        //get all the spots
//        List<Spot>spotList=parkingLot.getSpotList();
//
//        Spot reservedSpot=null;
//        Integer minPrice=0;
//
//        for(Spot spot:spotList)
//        {
//            if(spot.getOccupied()==Boolean.FALSE)
//            {
//                if(minPrice<spot.getPricePerHour())
//                {
//                    reservedSpot=spot;
//                    minPrice=spot.getPricePerHour();
//                }
//            }
//        }
//
//
//        if(reservedSpot==null)
//        {
//            throw new Exception("Cannot make reservation");
//        }
//
//
//        //now set all the attributes for reservation
//        Reservation reservation=new Reservation();
//
//        reservation.setUser(user);
//        reservation.setSpot(reservedSpot);
//        reservation.setNumberOfHours(timeInHours);
//
//        reservedSpot.getReservationList().add(reservation);
//        parkingLot.getSpotList().add(reservedSpot);
//        user.getReservationList().add(reservation);
//
//        userRepository3.save(user);
//        spotRepository3.save(reservedSpot);
//
//        reservationRepository3.save(reservation);
//
//        return reservation;



        try {

            if (!userRepository3.findById(userId).isPresent() || !parkingLotRepository3.findById(parkingLotId).isPresent()) {
                throw new Exception("Cannot make reservation");
            }

            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

            List<Spot> spotList = parkingLot.getSpotList();
            boolean checkForSpot = false;
            for (Spot spot : spotList) {
                if (!spot.getOccupied()) {
                    checkForSpot = true;
                    break;
                }
            }

            if (!checkForSpot) {
                throw new Exception("Cannot make reservation");
            }


            SpotType requestSpotType;

            if (numberOfWheels > 4) {
                requestSpotType = SpotType.OTHERS;
            } else if (numberOfWheels > 2) {
                requestSpotType = SpotType.FOUR_WHEELER;
            } else requestSpotType = SpotType.TWO_WHEELER;


            int minimumPrice = Integer.MAX_VALUE;

            checkForSpot = false;

            Spot spotChosen = null;

            for (Spot spot : spotList) {
                if (requestSpotType.equals(SpotType.OTHERS) && spot.getSpotType().equals(SpotType.OTHERS)) {
                    if (spot.getPricePerHour() * timeInHours < minimumPrice && !spot.getOccupied()) {
                        minimumPrice = spot.getPricePerHour() * timeInHours;
                        checkForSpot = true;
                        spotChosen = spot;
                    }
                } else if (requestSpotType.equals(SpotType.FOUR_WHEELER) && spot.getSpotType().equals(SpotType.OTHERS) ||
                        spot.getSpotType().equals(SpotType.FOUR_WHEELER)) {
                    if (spot.getPricePerHour() * timeInHours < minimumPrice && !spot.getOccupied()) {
                        minimumPrice = spot.getPricePerHour() * timeInHours;
                        checkForSpot = true;
                        spotChosen = spot;
                    }
                } else if (requestSpotType.equals(SpotType.TWO_WHEELER) && spot.getSpotType().equals(SpotType.OTHERS) ||
                        spot.getSpotType().equals(SpotType.FOUR_WHEELER) || spot.getSpotType().equals(SpotType.TWO_WHEELER)) {
                    if (spot.getPricePerHour() * timeInHours < minimumPrice && !spot.getOccupied()) {
                        minimumPrice = spot.getPricePerHour() * timeInHours;
                        checkForSpot = true;
                        spotChosen = spot;
                    }
                }

            }

            if (!checkForSpot) {
                throw new Exception("Cannot make reservation");
            }

            assert spotChosen != null;
            spotChosen.setOccupied(true);

            Reservation reservation = new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setSpot(spotChosen);
            reservation.setUser(user);

            //Bidirectional
            spotChosen.getReservationList().add(reservation);
            user.getReservationList().add(reservation);

            userRepository3.save(user);
            spotRepository3.save(spotChosen);

            return reservation;
        }
        catch (Exception e){
            return null;
        }

    }
}
