package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address)
    {
        //create new parking lot
        //then set name and address
        ParkingLot parkingLot=new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);

        //then add to db
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour)
    {
        //get the parkingLot from the db by parkingId
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();

        //create new Spot
        Spot spot=new Spot();



        //set spotType

        if(numberOfWheels>4)
        {
            spot.setSpotType(SpotType.OTHERS);

        }
        else if(numberOfWheels>2)
        {
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }
        else
        {
            spot.setSpotType(SpotType.TWO_WHEELER);
        }

        //set  parkingLot
        spot.setParkingLot(parkingLot);

        //set pricePerHour
        spot.setPricePerHour(pricePerHour);

        //set occupied as false
        spot.setOccupied(Boolean.FALSE);

        parkingLot.getSpotList().add(spot);

        //save into spotRepository
       // spotRepository1.save(spot);

        parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteSpot(int spotId)
    {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour)
    {
        
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();

        List<Spot>spotList=parkingLot.getSpotList();

        Spot temp = null;

        for(Spot spot1:spotList)
        {
           //check which spot.getId() is equal to spotId
            if(spot1.getId()==spotId)
            {
                temp=spot1;
                break;
            }
        }


        if(temp!=null)
        {
            temp.setParkingLot(parkingLot);
            temp.setPricePerHour(pricePerHour);

            spotRepository1.save(temp);
        }

        return temp;
    }

    @Override
    public void deleteParkingLot(int parkingLotId)
    {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
