package com.yu.rentalmyfavorite.converter;


import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import org.springframework.core.convert.converter.Converter;

public class RentalMyFAVToAddToWishListConverter implements Converter<RentalMyFavorite, AddToWishList> {

    @Override
    public AddToWishList convert(RentalMyFavorite rentalMyFavorite) {
        AddToWishList addToWishList = new AddToWishList();
        addToWishList.setRentalNo(rentalMyFavorite.getCompositeKey().getRentalNo());
        addToWishList.setMemNo(rentalMyFavorite.getCompositeKey().getMemNo());
        addToWishList.setRentalFavTime(rentalMyFavorite.getRentalFavTime().toString());
        return addToWishList;
    }
}
