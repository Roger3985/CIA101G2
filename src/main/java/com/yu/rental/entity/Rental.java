package com.yu.rental.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.howard.rentalorderdetails.entity.RentalOrderDetails;
import com.yu.rentalcategory.entity.RentalCategory;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import com.yu.rentalpic.entity.RentalPic;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity  //標示類別為"永續類別"
@Table(name = "rental")  //此"永續類別"對應到的表格
public class Rental implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public Rental(Integer rentalNo, String rentalName, BigDecimal rentalPrice, Integer rentalSize, String rentalColor,
                  String rentalInfo, Byte rentalStat) {
    }

    // 分組驗證 (ex. Add時是以自動分配的數字，但Update也許對應不到。固可使用此方法)
    public static interface AddRentalGroup{};
    public static interface UpdateRentalGroup{};


    @Id //標示為PK
    @Column(name="rentalno")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //有設立AUTO_INCREMENT
    private Integer rentalNo;


    @Column(name="rentalname", length=40)
    @NotBlank(message="*租借品名稱: 請勿空白", groups = {AddRentalGroup.class,UpdateRentalGroup.class})
    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,40}$",message = "*只能是中、英文字母、數字和_ , 且長度必需在2到40之間",
            groups = {AddRentalGroup.class,UpdateRentalGroup.class})
    private String rentalName;


    @Column(name="rentalprice",columnDefinition="BigDecimal")
    @NotNull(message="*租借品單價: 請勿空白", groups = {AddRentalGroup.class,UpdateRentalGroup.class})
    @DecimalMin(value = "00000", message = "*租借品單價: 不能小於0",
            groups = {AddRentalGroup.class,UpdateRentalGroup.class})
    private BigDecimal rentalPrice;


    @Column(name="rentalsize")
    @NotNull(message="*尺寸: 請勿空白", groups = {AddRentalGroup.class,UpdateRentalGroup.class})
    @Min(value = 0, message = "*尺寸: 不能小於{value}", groups = {AddRentalGroup.class,UpdateRentalGroup.class})
    @Max(value = 5, message = "*尺寸: 不能小於{value}", groups = {AddRentalGroup.class,UpdateRentalGroup.class})
    private Integer rentalSize;


    @Column(name="rentalcolor", length=10)
    @NotBlank(message="*顏色: 請勿空白", groups = {AddRentalGroup.class,UpdateRentalGroup.class})
    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "*顏色: 只能是中文, 且長度必需在2到10之間",
            groups = {AddRentalGroup.class,UpdateRentalGroup.class})
    private String rentalColor;


    @Column(name="rentalinfo", length=1000)
    @NotBlank(message="*租借品資訊: 請勿空白", groups = {AddRentalGroup.class,UpdateRentalGroup.class})
    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,1000}$",
            message = "*租借品資訊: 只能是中、英文字母、數字和_ , 且長度必需在2到1000之間",
            groups = {AddRentalGroup.class,UpdateRentalGroup.class})
    private String rentalInfo;


    @Column(name="rentalstat",columnDefinition = "TINYINT")
    @NotNull(message="*租借品狀態: 請勿空白", groups = {AddRentalGroup.class,UpdateRentalGroup.class})
    @Min(value = 0, message = "*租借品狀態: 不能小於{value}", groups = {AddRentalGroup.class,UpdateRentalGroup.class})
    @Max(value = 5, message = "*租借品狀態: 不能小於{value}", groups = {AddRentalGroup.class,UpdateRentalGroup.class})
    private Byte rentalStat;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "rentalcatno", referencedColumnName = "rentalcatno") //對應rental的rCatNo
    private RentalCategory rentalCategory;

    @JsonBackReference
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private Set<RentalOrderDetails> rentalOrderDetails;


    @JsonBackReference
    @OneToMany(mappedBy = "rental",cascade = CascadeType.ALL)
    private Set<RentalMyFavorite> rentalMyFavorites;


    @JsonBackReference
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private Set<RentalPic> rentalPics;

    public Rental() {
    }

    public Rental(Integer rentalNo, String rentalName,BigDecimal rentalPrice, Integer rentalSize, String rentalColor, String rentalInfo, Byte rentalStat,
                  RentalCategory rentalCategory, Set<RentalOrderDetails> rentalOrderDetails,
                  Set<RentalMyFavorite> rentalMyFavorites, Set<RentalPic> rentalPics) {
        this.rentalNo = rentalNo;
        this.rentalName = rentalName;
        this.rentalPrice = rentalPrice;
        this.rentalSize = rentalSize;
        this.rentalColor = rentalColor;
        this.rentalInfo = rentalInfo;
        this.rentalStat = rentalStat;
        this.rentalCategory = rentalCategory;
        this.rentalOrderDetails = rentalOrderDetails;
        this.rentalMyFavorites = rentalMyFavorites;
        this.rentalPics = rentalPics;
    }

    public Integer getRentalNo() {
        return rentalNo;
    }

    public void setRentalNo(Integer rentalNo) {
        this.rentalNo = rentalNo;
    }

    public RentalCategory getRentalCategory() {
        return rentalCategory;
    }

    public void setRentalCategory(RentalCategory rentalCategory) {
        this.rentalCategory = rentalCategory;
    }

    public String getRentalName() {
        return rentalName;
    }

    public void setRentalName(String rentalName) {
        this.rentalName = rentalName;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public Integer getRentalSize() {
        return rentalSize;
    }

    public void setRentalSize(Integer rentalSize) {
        this.rentalSize = rentalSize;
    }

    public String getRentalColor() {
        return rentalColor;
    }

    public void setRentalColor(String rentalColor) {
        this.rentalColor = rentalColor;
    }

    public String getRentalInfo() {
        return rentalInfo;
    }

    public void setRentalInfo(String rentalInfo) {
        this.rentalInfo = rentalInfo;
    }

    public Byte getRentalStat() {
        return rentalStat;
    }

    public void setRentalStat(Byte rentalStat) {
        this.rentalStat = rentalStat;
    }

    public Set<RentalOrderDetails> getRentalOrderDetails() {
        return rentalOrderDetails;
    }

    public void setRentalOrderDetails(Set<RentalOrderDetails> rentalOrderDetails) {
        this.rentalOrderDetails = rentalOrderDetails;
    }

    public Set<RentalMyFavorite> getRentalMyFavorites() {
        return rentalMyFavorites;
    }

    public void setRentalMyFavorites(Set<RentalMyFavorite> rentalMyFavorites) {
        this.rentalMyFavorites = rentalMyFavorites;
    }

    public Set<RentalPic> getRentalPics() {
        return rentalPics;
    }

    public void setRentalPics(Set<RentalPic> rentalPics) {
        this.rentalPics = rentalPics;
    }
}