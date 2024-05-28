package com.ren.product.service.impl;

import com.ren.product.dto.ProductDTO;
import com.ren.product.entity.Product;
import com.ren.product.dao.ProductRepository;
import com.ren.product.service.ProductService_interface;
import com.ren.productcategory.dao.ProductCategoryRepository;
import com.ren.productcategory.service.impl.ProductCategoryServiceImpl;
import com.ren.productpicture.entity.ProductPicture;
import com.ren.productpicture.service.impl.ProductPictureServiceImpl;
import com.ren.productreview.entity.ProductReview;
import com.ren.productreview.service.impl.ProductReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.ren.util.Constants.*;

@Service
public class ProductServiceImpl implements ProductService_interface {

    private final int SUCCESS = 1;
    private final int FAILURE = -1;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryServiceImpl productCategorySvc;

    @Autowired
    private ProductReviewServiceImpl productReviewSvc;

    @Autowired
    @Qualifier("proStrDTO")
    private RedisTemplate<String, ProductDTO> proDTORedisTemplate;
    @Autowired
    private ProductPictureServiceImpl productPictureSvc;

    /**
     * 新增單項商品
     *
     * @param product 前端輸入商品資訊
     * @return 返回更新後Entity
     */
    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * 批量新增
     *
     * @param list 商品清單
     * @return 返回新增後清單
     */
    @Override
    public List<Product> addProductList(List<Product> list) {
        return productRepository.saveAll(list);
    }

    /**
     * 查詢單項商品
     *
     * @param productNo 商品編號
     * @return 返回查詢資料
     */
    @Override
    public Product getOneProduct(Integer productNo) {
        return productRepository.findById(productNo).orElse(null);
    }

    /**
     * 查詢全部商品資料
     *
     * @return 返回查詢清單
     */
    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    /**
     * 關鍵字查詢
     *
     * @param keyword 關鍵字
     * @return 返回相關聯資料
     */
    @Override
    public List<Product> searchByKeyword(String keyword) {
        return productRepository.searchByKeyword(keyword);
    }

    /**
     * 根據商品類別編號查詢商品
     *
     * @param productCatNo 商品類別編號
     * @return 返回相同商品編號的商品清單
     */
    @Override
    public List<Product> getByProductCatNo(Integer productCatNo) {
        return productRepository.findProductsByProductCategory_ProductCatNo(productCatNo);
    }

    /**
     * 根據商品類別名稱查詢商品
     *
     * @param productCatName 商品類別名稱
     * @return 返回相同商品類別名稱的商品清單
     */
    @Override
    public List<Product> getByProductCatName(String productCatName) {
        return productRepository.findProductsByProductCategory_ProductCatName(productCatName);
    }

    /**
     * 獲得全部上架或全部下架的商品清單
     *
     * @param productStat 商品上下架狀態
     * @return 返回相同狀態的商品清單
     */
    @Override
    public List<Product> getByProductStat(Byte productStat) {
        return productRepository.findByProductStat(productStat);
    }

    /**
     * 最新上架時間前10名
     *
     * @return 返回前10名的清單
     */
    @Override
    public List<Product> getTopOnShelf() {
        return productRepository.findTop1OByOrderByProductOnShelfDesc();
    }

    /**
     * 下架時間前10名
     *
     * @return 返回前10名的清單
     */
    @Override
    public List<Product> getTopOffShelf() {
        return productRepository.findTop10ByOrderByProductOffShelfDesc();
    }

    /**
     * 萬用複合查詢
     * 預計在前台使用標籤過濾搜尋結果使用
     *
     * @return 返回相關聯商品清單
     */
    @Override
    public List<Product> getComposite(Product product) {
        return productRepository.findByAttributes(product.getProductNo(), product.getProductCategory().getProductCatNo(), product.getProductName(), product.getProductInfo(), product.getProductSize(), product.getProductColor(), product.getProductPrice(), product.getProductStat(), product.getProductOnShelf(), product.getProductOffShelf());
    }

    @Override
    public List<Product> getVisitProducts(Integer productCatNo, String productName) {
        return productRepository.findProductsByProductCategory_ProductCatNoAndProductName(productCatNo, productName);
    }

    @Override
    public List<Product> getDifSize(Integer productCatNo, String productName, String productColor) {
        return productRepository.findProductsByProductCategory_ProductCatNoAndProductNameAndProductColor(productCatNo, productName, productColor);
    }

    @Override
    public List<Product> getDifColor(Integer productCatNo, String productName, Integer productSize) {
        return productRepository.findProductsByProductCategory_ProductCatNoAndProductNameAndProductSize(productCatNo, productName, productSize);
    }

    @Override
    public List<Product> getMyProduct(Integer productCatNo, String productName, String productColor, Integer productSize) {
        return productRepository.findProductsByProductCategory_ProductCatNoAndProductNameAndProductColorAndProductSize(productCatNo, productName, productColor, productSize);
    }

    /**
     * 全文搜索
     *
     * @param keyword 關鍵字
     * @param page    指定為第幾頁
     * @param size    指定分頁要呈現多少筆資料
     * @return 返回分頁搜尋結果
     */
    @Override
    public Page<Product> searchProducts(String keyword, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(keyword, pageable);
    }

    @Override
    public List<Product> getProductsByColor(String productColor) {
        return productRepository.findProductsByProductColor(productColor);
    }

    @Override
    public List<Product> getProductsBySize(Integer productSize) {
        return productRepository.findProductsByProductSize(productSize);
    }

    @Override
    public List<Product> getProductsByPrice(BigDecimal minProductPrice, BigDecimal maxProductPrice) {
        return productRepository.findProductsByProductPriceBetween(minProductPrice, maxProductPrice);
    }

    /**
     * 更新單筆商品資料
     *
     * @param product 欲更新商品Entity
     * @return 返回更新後商品Entity
     */
    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * 批量更新商品資料
     *
     * @param list 欲更新商品清單
     * @return 返回更新後商品清單
     */
    @Override
    public List<Product> updateProducts(List<Product> list) {
        return productRepository.saveAll(list);
    }

    /**
     * 將商品上架
     *
     * @param productNo 欲上架商品
     */
    @Override
    public void onShelf(Integer productNo) {
        productRepository.updateProductOnShelf(productNo, OnShelf, Timestamp.valueOf(LocalDateTime.now()));
    }

    /**
     * 將商品下架
     *
     * @param productNo 欲下架商品
     */
    @Override
    public void offShelf(Integer productNo) {
        productRepository.updateProductOffShelf(productNo, OffShelf, Timestamp.valueOf(LocalDateTime.now()));
    }

    /**
     * 批量上架商品
     *
     * @param list 欲上架商品清單
     */
    @Override
    public void listOnShelf(List<Integer> list) {
        productRepository.updateProductOnShelfBatch(list, OnShelf, Timestamp.valueOf(LocalDateTime.now()));
    }

    /**
     * 批量下架商品
     *
     * @param list 欲下架商品清單
     */
    @Override
    public void listOffShelf(List<Integer> list) {
        productRepository.updateProductOffShelfBatch(list, OffShelf, Timestamp.valueOf(LocalDateTime.now()));
    }

    /**
     * 依商品類別編號將同一類別商品上架
     *
     * @param productCatNo 欲上架之商品類別編號
     */
    @Override
    public void OnShelfByProductCatNo(Integer productCatNo) {
        productRepository.updateProductOnShelfByCategoryNo(productCatNo, OnShelf, Timestamp.valueOf(LocalDateTime.now()));
    }

    /**
     * 依商品類別編號將同一類別商品下架
     *
     * @param productCatNo 欲下架之商品類別編號
     */
    @Override
    public void OffShelfByProductCatNo(Integer productCatNo) {
        productRepository.updateProductOffShelfByCategoryNo(productCatNo, OffShelf, Timestamp.valueOf(LocalDateTime.now()));
    }

    /**
     * 依商品類別名稱將同一類別商品上架
     *
     * @param productCatName 欲上架之商品類別名稱
     */
    @Override
    public void OnShelfByProductCatName(String productCatName) {
        productRepository.updateProductOnShelfByCategoryName(productCatName, OnShelf, Timestamp.valueOf(LocalDateTime.now()));
    }

    /**
     * 依商品類別名稱將同一類別商品下架
     *
     * @param productCatName 欲下架之商品類別名稱
     */
    @Override
    public void OffShelfByProductCatName(String productCatName) {
        productRepository.updateProductOffShelfByCategoryName(productCatName, OffShelf, Timestamp.valueOf(LocalDateTime.now()));
    }

    /**
     * 刪除單項商品
     *
     * @param productNo 欲刪除之商品編號
     */
    @Override
    public void deleteProduct(Integer productNo) {
        productRepository.deleteById(productNo);
    }

    /**
     * 依商品類別編號刪除商品
     *
     * @param productCatNo 欲刪除商品類別編號
     */
    @Override
    public void deleteByProductCatNo(Integer productCatNo) {
        productRepository.deleteProductsByProductCategory_ProductCatNo(productCatNo);
    }

    /**
     * 依商品類別名稱刪除商品
     *
     * @param productCatName 欲刪除商品類別名稱
     */
    @Override
    public void deleteByProductCatName(String productCatName) {
        productRepository.deleteProductsByProductCategory_ProductCatName(productCatName);
    }

    public void redisDataStored() {
        var productUniqueKeySet = new HashSet<String>();
        List<Product> productList = getByProductStat(Byte.valueOf(ON_SHELF));
        // 以商品類別編號-商品名稱作為uniqueKey
        for (Product product : productList) {
            productUniqueKeySet.add(product.getProductCategory().getProductCatNo() + "-" + product.getProductName());
        }

        for (var key : productUniqueKeySet) {
            ProductDTO productDTO = new ProductDTO();
            String[] parts = key.split("-");
            Integer productCatNo = Integer.valueOf(parts[0]);
            String productName = parts[1];
            List<Product> list = getVisitProducts(productCatNo, productName);
            productDTO.setProductID(key);
            productDTO.setProductCatNo(productCatNo);
            String productCatName = productCategorySvc.getOneProductCategory(productCatNo).getProductCatName();
            productDTO.setProductCatName(productCatName);
            productDTO.setProductName(productName);
            var productNoList = new ArrayList<Integer>();
            var productPicNoList = new ArrayList<Integer>();
            HashSet<BigDecimal> productPriceSet = new HashSet<>();
            HashSet<Integer> productSizeSet = new HashSet<>();
            HashSet<String> productColorSet = new HashSet<>();
            HashSet<Timestamp> productOnShelfSet = new HashSet<>();
            Integer productTotalScore = 0;
            Integer productScorePeople = 0;
            Double productScore = 0.0;
            for (Product product : list) {
                Integer productNo = product.getProductNo();
                productNoList.add(productNo);
                productPriceSet.add(product.getProductPrice());

                if (product.getProductSize() != null) {
                    productSizeSet.add(product.getProductSize());
                }

                if (product.getProductColor() != null) {
                    productColorSet.add(product.getProductColor());
                }

                if (product.getProductOnShelf() != null) {
                    productOnShelfSet.add(product.getProductOnShelf());
                }

                List<ProductReview> productReviews = productReviewSvc.getByProductNo(product.getProductNo());

                for (ProductReview productReview : productReviews) {
                    productTotalScore += productReview.getProductScore();
                    productScorePeople++;
                }

                List<ProductPicture> productPictures = productPictureSvc.getByProductNo(productNo);
                for (var productPicture : productPictures) {
                    productPicNoList.add(productPicture.getProductPicNo());
                }
            }

            if (productScorePeople != 0) {
                productScore = (double) productTotalScore / productScorePeople;
                productScore = Math.round(productScore * 10.0) / 10.0;
            }

            productDTO.setProductScorePeople(productScorePeople);
            productDTO.setProductScore(productScore);
            productDTO.setProductNoList(productNoList);
            if (productPicNoList.isEmpty()) {
                productPicNoList.add(1);
            }
            productDTO.setProductPicNoList(productPicNoList);
            productDTO.setProductPriceSet(productPriceSet);
            productDTO.setProductSizeSet(productSizeSet);
            productDTO.setProductColorSet(productColorSet);
            productDTO.setProductOnShelfSet(productOnShelfSet);

            proDTORedisTemplate.opsForValue().set(key, productDTO);
        }
    }

    public List<ProductDTO> getVisitProduct(List<Product> productList) {
        var productUniqueKeySet = new HashSet<String>();

        // 以商品類別編號-商品名稱作為uniqueKey
        for (Product product : productList) {
            productUniqueKeySet.add(product.getProductCategory().getProductCatNo() + "-" + product.getProductName());
        }

        List<ProductDTO> productDTOList = new ArrayList<>();
        for (var key : productUniqueKeySet) {
            ProductDTO productDTO = new ProductDTO();
            String[] parts = key.split("-");
            Integer productCatNo = Integer.valueOf(parts[0]);
            String productName = parts[1];
            List<Product> list = getVisitProducts(productCatNo, productName);
            productDTO.setProductID(key);
            productDTO.setProductCatNo(productCatNo);
            String productCatName = productCategorySvc.getOneProductCategory(productCatNo).getProductCatName();
            productDTO.setProductCatName(productCatName);
            productDTO.setProductName(productName);
            List<Integer> productNoList = new ArrayList<>();
            HashSet<BigDecimal> productPriceSet = new HashSet<>();
            HashSet<Integer> productSizeSet = new HashSet<>();
            HashSet<String> productColorSet = new HashSet<>();
            HashSet<Timestamp> productOnShelfSet = new HashSet<>();
            Integer productTotalScore = 0;
            Integer productScorePeople = 0;
            Double productScore = 0.0;
            for (Product product : list) {
                productNoList.add(product.getProductNo());
                productPriceSet.add(product.getProductPrice());
                if (product.getProductSize() != null) {
                    productSizeSet.add(product.getProductSize());
                }

                if (product.getProductColor() != null) {
                    productColorSet.add(product.getProductColor());
                }

                if (product.getProductOnShelf() != null) {
                    productOnShelfSet.add(product.getProductOnShelf());
                }
                List<ProductReview> productReviews = productReviewSvc.getByProductNo(product.getProductNo());
                for (ProductReview productReview : productReviews) {
                    productTotalScore += productReview.getProductScore();
                    productScorePeople++;
                }

            }
            if (productScorePeople != 0) {
                productScore = (double) productTotalScore / productScorePeople;
                productScore = Math.round(productScore * 10.0) / 10.0;
            }
            productDTO.setProductScorePeople(productScorePeople);
            productDTO.setProductScore(productScore);
            productDTO.setProductPriceSet(productPriceSet);
            productDTO.setProductSizeSet(productSizeSet);
            productDTO.setProductColorSet(productColorSet);
            productDTO.setProductOnShelfSet(productOnShelfSet);
            productDTOList.add(productDTO);
            System.out.println("productDTOListNumber: " + productDTOList.size());
        }
        return productDTOList;
    }

    public Page<ProductDTO> getVisitProduct(List<Product> productList, Pageable pageable) {
        // 計算uniqueKeySet大小
        var productUniqueKeySet = new HashSet<String>();
        for (Product product : productList) {
            productUniqueKeySet.add(product.getProductCategory().getProductCatNo() + "-" + product.getProductName());
        }

        int totalSize = productUniqueKeySet.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), totalSize);

        List<ProductDTO> productDTOList = new ArrayList<>();
        int currentIndex = 0;

        for (var key : productUniqueKeySet) {
            if (currentIndex >= start && currentIndex < end) {
                ProductDTO productDTO = new ProductDTO();
                String[] parts = key.split("-");
                Integer productCatNo = Integer.valueOf(parts[0]);
                String productName = parts[1];
                List<Product> list = getVisitProducts(productCatNo, productName);
                productDTO.setProductID(key);
                productDTO.setProductCatNo(productCatNo);
                String productCatName = productCategorySvc.getOneProductCategory(productCatNo).getProductCatName();
                productDTO.setProductCatName(productCatName);
                productDTO.setProductName(productName);
                List<Integer> productNoList = new ArrayList<>();
                HashSet<BigDecimal> productPriceSet = new HashSet<>();
                HashSet<Integer> productSizeSet = new HashSet<>();
                HashSet<String> productColorSet = new HashSet<>();
                HashSet<Timestamp> productOnShelfSet = new HashSet<>();
                Integer productTotalScore = 0;
                Integer productScorePeople = 0;
                Double productScore = 0.0;
                for (Product product : list) {
                    productNoList.add(product.getProductNo());
                    productPriceSet.add(product.getProductPrice());
                    if (product.getProductSize() != null) {
                        productSizeSet.add(product.getProductSize());
                    }

                    if (product.getProductColor() != null) {
                        productColorSet.add(product.getProductColor());
                    }

                    if (product.getProductOnShelf() != null) {
                        productOnShelfSet.add(product.getProductOnShelf());
                    }
                    List<ProductReview> productReviews = productReviewSvc.getByProductNo(product.getProductNo());
                    for (ProductReview productReview : productReviews) {
                        productTotalScore += productReview.getProductScore();
                        productScorePeople++;
                    }

                }

                if (productScorePeople != 0) {
                    productScore = (double) productTotalScore / productScorePeople;
                    productScore = Math.round(productScore * 10.0) / 10.0;
                }

                productDTO.setProductScorePeople(productScorePeople);
                productDTO.setProductScore(productScore);
                productDTO.setProductNoList((ArrayList<Integer>) productNoList);
                productDTO.setProductPriceSet(productPriceSet);
                productDTO.setProductSizeSet(productSizeSet);
                productDTO.setProductColorSet(productColorSet);
                productDTO.setProductOnShelfSet(productOnShelfSet);
                productDTOList.add(productDTO);
            }
            currentIndex++;
        }

        return new PageImpl<>(productDTOList, pageable, totalSize);
    }

    public Page<ProductDTO> getVisitProduct(List<Product> productList, Pageable pageable, String sortType) {
        // 計算uniqueKeySet大小
        var productUniqueKeySet = new HashSet<String>();
        for (Product product : productList) {
            productUniqueKeySet.add(product.getProductCategory().getProductCatNo() + "-" + product.getProductName());
        }

        List<ProductDTO> productDTOList = new ArrayList<>();
        for (var key : productUniqueKeySet) {
            ProductDTO productDTO = new ProductDTO();
            String[] parts = key.split("-");
            Integer productCatNo = Integer.valueOf(parts[0]);
            String productName = parts[1];
            List<Product> list = getVisitProducts(productCatNo, productName);
            productDTO.setProductID(key);
            productDTO.setProductCatNo(productCatNo);
            String productCatName = productCategorySvc.getOneProductCategory(productCatNo).getProductCatName();
            productDTO.setProductCatName(productCatName);
            productDTO.setProductName(productName);
            List<Integer> productNoList = new ArrayList<>();
            HashSet<BigDecimal> productPriceSet = new HashSet<>();
            HashSet<Integer> productSizeSet = new HashSet<>();
            HashSet<String> productColorSet = new HashSet<>();
            HashSet<Timestamp> productOnShelfSet = new HashSet<>();
            Integer productTotalScore = 0;
            Integer productScorePeople = 0;
            Double productScore = 0.0;
            for (Product product : list) {
                productNoList.add(product.getProductNo());
                productPriceSet.add(product.getProductPrice());
                if (product.getProductSize() != null) {
                    productSizeSet.add(product.getProductSize());
                }
                if (product.getProductColor() != null) {
                    productColorSet.add(product.getProductColor());
                }
                if (product.getProductOnShelf() != null) {
                    productOnShelfSet.add(product.getProductOnShelf());
                }
                List<ProductReview> productReviews = productReviewSvc.getByProductNo(product.getProductNo());
                for (ProductReview productReview : productReviews) {
                    productTotalScore += productReview.getProductScore();
                    productScorePeople++;
                }
            }
            if (productScorePeople != 0) {
                productScore = (double) productTotalScore / productScorePeople;
                productScore = Math.round(productScore * 10.0) / 10.0;
            }
            productDTO.setProductScorePeople(productScorePeople);
            productDTO.setProductScore(productScore);
            productDTO.setProductNoList((ArrayList<Integer>) productNoList);
            productDTO.setProductPriceSet(productPriceSet);
            productDTO.setProductSizeSet(productSizeSet);
            productDTO.setProductColorSet(productColorSet);
            productDTO.setProductOnShelfSet(productOnShelfSet);
            productDTOList.add(productDTO);
        }

        // 根據 sortType 排序
        switch (sortType) {
            case "newest":
                productDTOList.sort(Comparator.comparing(
                        p -> p.getProductOnShelfSet().stream().findFirst().orElse(null),
                        Comparator.nullsLast(Comparator.reverseOrder())));
                break;
            case "rating":
                productDTOList.sort(Comparator.comparing(ProductDTO::getProductScore, Comparator.nullsLast(Comparator.reverseOrder())));
                break;
            case "priceLowToHigh":
                productDTOList.sort(Comparator.comparing(
                        p -> p.getProductPriceSet().stream().findFirst().orElse(null),
                        Comparator.nullsLast(Comparator.naturalOrder())));
                break;
            case "priceHighToLow":
                productDTOList.sort(Comparator.comparing(
                        p -> p.getProductPriceSet().stream().findFirst().orElse(null),
                        Comparator.nullsLast(Comparator.reverseOrder())));
                break;
            default:
                break;
        }

        int totalSize = productDTOList.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), totalSize);

        List<ProductDTO> pagedProductDTOList = productDTOList.subList(start, end);

        return new PageImpl<>(pagedProductDTOList, pageable, totalSize);
    }

    public Page<ProductDTO> getVisitProduct(List<Product> productList, Pageable pageable, Map<String, String> filters) {
        // 计算uniqueKeySet大小
        var productUniqueKeySet = new HashSet<String>();
        for (Product product : productList) {
            productUniqueKeySet.add(product.getProductCategory().getProductCatNo() + "-" + product.getProductName());
        }

        List<ProductDTO> productDTOList = new ArrayList<>();
        for (var key : productUniqueKeySet) {
            ProductDTO productDTO = new ProductDTO();
            String[] parts = key.split("-");
            Integer productCatNo = Integer.valueOf(parts[0]);
            String productName = parts[1];
            List<Product> list = getVisitProducts(productCatNo, productName);
            productDTO.setProductID(key);
            productDTO.setProductCatNo(productCatNo);
            String productCatName = productCategorySvc.getOneProductCategory(productCatNo).getProductCatName();
            productDTO.setProductCatName(productCatName);
            productDTO.setProductName(productName);
            List<Integer> productNoList = new ArrayList<>();
            HashSet<BigDecimal> productPriceSet = new HashSet<>();
            HashSet<Integer> productSizeSet = new HashSet<>();
            HashSet<String> productColorSet = new HashSet<>();
            HashSet<Timestamp> productOnShelfSet = new HashSet<>();
            Integer productTotalScore = 0;
            Integer productScorePeople = 0;
            Double productScore = 0.0;
            for (Product product : list) {
                productNoList.add(product.getProductNo());
                productPriceSet.add(product.getProductPrice());
                if (product.getProductSize() != null) {
                    productSizeSet.add(product.getProductSize());
                }
                if (product.getProductColor() != null) {
                    productColorSet.add(product.getProductColor());
                }
                if (product.getProductOnShelf() != null) {
                    productOnShelfSet.add(product.getProductOnShelf());
                }
                List<ProductReview> productReviews = productReviewSvc.getByProductNo(product.getProductNo());
                for (ProductReview productReview : productReviews) {
                    productTotalScore += productReview.getProductScore();
                    productScorePeople++;
                }
            }
            if (productScorePeople != 0) {
                productScore = (double) productTotalScore / productScorePeople;
                productScore = Math.round(productScore * 10.0) / 10.0;
            }
            productDTO.setProductScorePeople(productScorePeople);
            productDTO.setProductScore(productScore);
            productDTO.setProductNoList((ArrayList<Integer>) productNoList);
            productDTO.setProductPriceSet(productPriceSet);
            productDTO.setProductSizeSet(productSizeSet);
            productDTO.setProductColorSet(productColorSet);
            productDTO.setProductOnShelfSet(productOnShelfSet);
            productDTOList.add(productDTO);
        }

        if (filters != null) {

            if (filters.containsKey("color")) {
                String[] colors = filters.get("color").split(",");
                productDTOList = productDTOList.stream()
                        .filter(productDTO -> Arrays.stream(colors).anyMatch(color -> productDTO.getProductColorSet().contains(color)))
                        .collect(Collectors.toList());
            }

            if (filters.containsKey("size")) {
                String[] sizes = filters.get("size").split(",");
                List<Integer> sizeList = Arrays.stream(sizes).map(Integer::valueOf).collect(Collectors.toList());
                productDTOList = productDTOList.stream()
                        .filter(productDTO -> productDTO.getProductSizeSet().stream().anyMatch(sizeList::contains))
                        .collect(Collectors.toList());
            }

            if (filters.containsKey("priceRange")) {
                String[] priceRanges = filters.get("priceRange").split(",");
                productDTOList = productDTOList.stream()
                        .filter(productDTO -> {
                            for (String range : priceRanges) {
                                String[] prices = range.split("-");
                                BigDecimal minPrice = new BigDecimal(prices[0]);
                                BigDecimal maxPrice = new BigDecimal(prices[1]);
                                if (productDTO.getProductPriceSet().stream().anyMatch(price -> price.compareTo(minPrice) >= 0 && price.compareTo(maxPrice) <= 0)) {
                                    return true;
                                }
                            }
                            return false;
                        })
                        .collect(Collectors.toList());
            }
        }

        int totalSize = productDTOList.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), totalSize);

        List<ProductDTO> pagedProductDTOList = productDTOList.subList(start, end);

        return new PageImpl<>(pagedProductDTOList, pageable, totalSize);
    }


    public ProductDTO getOneProductDTO(Integer productNo) {
        ProductDTO productDTO = new ProductDTO();
        Product product = getOneProduct(productNo);
        if (product != null) {
            Integer productCatNo = product.getProductCategory().getProductCatNo();
            String productName = product.getProductName();
            productDTO.setProductID(productCatNo + "-" + productName);
            productDTO.setProductCatNo(productCatNo);
            productDTO.setProductName(productName);
            productDTO.setProductCatName(product.getProductCategory().getProductCatName());
            List<Product> list = getVisitProducts(productCatNo, productName);
            List<Integer> productNoList = new ArrayList<>();
            HashSet<BigDecimal> productPriceSet = new HashSet<>();
            HashSet<Integer> productSizeSet = new HashSet<>();
            HashSet<String> productColorSet = new HashSet<>();
            HashSet<Timestamp> productOnShelfSet = new HashSet<>();
            Integer productTotalScore = 0;
            Integer productScorePeople = 0;
            Double productScore = 0.0;
            for (Product pro : list) {
                productNoList.add(pro.getProductNo());
                productPriceSet.add(pro.getProductPrice());
                if (pro.getProductSize() != null) {
                    productSizeSet.add(pro.getProductSize());
                }

                if (pro.getProductColor() != null) {
                    productColorSet.add(pro.getProductColor());
                }

                if (product.getProductOnShelf() != null) {
                    productOnShelfSet.add(pro.getProductOnShelf());
                }
                List<ProductReview> productReviews = productReviewSvc.getByProductNo(pro.getProductNo());
                for (ProductReview productReview : productReviews) {
                    productTotalScore += productReview.getProductScore();
                    productScorePeople++;
                }

            }
            if (productScorePeople != 0) {
                productScore = (double) productTotalScore / productScorePeople;
                productScore = Math.round(productScore * 10.0) / 10.0;
            }
            productDTO.setProductScorePeople(productScorePeople);
            productDTO.setProductScore(productScore);
            productDTO.setProductNoList((ArrayList<Integer>) productNoList);
            productDTO.setProductPriceSet(productPriceSet);
            productDTO.setProductSizeSet(productSizeSet);
            productDTO.setProductColorSet(productColorSet);
            productDTO.setProductOnShelfSet(productOnShelfSet);
        }
        return productDTO;
    }

    public ProductDTO getOneDTOFromRedis(Integer productNo) {
        Product product = getOneProduct(productNo);
        Integer productCatNo = product.getProductCategory().getProductCatNo();
        String productName = product.getProductName();
        return proDTORedisTemplate.opsForValue().get(productCatNo + "-" + productName);
    }

    public List<ProductDTO> getAllFromRedis() {
        Set<String> keys = proDTORedisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            try {
                return proDTORedisTemplate.opsForValue().multiGet(keys);
            } catch (Exception e) {
                // 添加日志记录以捕捉反序列化时的错误
                System.err.println("Error deserializing ProductDTO from Redis: " + e.getMessage());
                e.printStackTrace();
            }
        }
        // 如果沒資料就重新抓一次
        List<ProductDTO> list = getVisitProduct(getAll());
        redisDataStored();
        return list;
    }

    public Page<ProductDTO> getVisitProductFromRedis(Pageable pageable, String sortType) {

        List<ProductDTO> productDTOList = getAllFromRedis();

        // 根據 sortType 排序
        switch (sortType) {
            case "newest":
                productDTOList.sort(Comparator.comparing(
                        p -> p.getProductOnShelfSet().stream().findFirst().orElse(null),
                        Comparator.nullsLast(Comparator.reverseOrder())));
                break;
            case "rating":
                productDTOList.sort(Comparator.comparing(ProductDTO::getProductScore, Comparator.nullsLast(Comparator.reverseOrder())));
                break;
            case "priceLowToHigh":
                productDTOList.sort(Comparator.comparing(
                        p -> p.getProductPriceSet().stream().findFirst().orElse(null),
                        Comparator.nullsLast(Comparator.naturalOrder())));
                break;
            case "priceHighToLow":
                productDTOList.sort(Comparator.comparing(
                        p -> p.getProductPriceSet().stream().findFirst().orElse(null),
                        Comparator.nullsLast(Comparator.reverseOrder())));
                break;
            default:
                break;
        }

        int totalSize = productDTOList.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), totalSize);

        List<ProductDTO> pagedProductDTOList = productDTOList.subList(start, end);

        return new PageImpl<>(pagedProductDTOList, pageable, totalSize);
    }

    public Page<ProductDTO> getVisitProductFromRedis(Pageable pageable, Map<String, String> filters) {
        List<ProductDTO> productDTOList = getAllFromRedis();

        if (filters != null) {
            if (filters.containsKey("color")) {
                String[] colors = filters.get("color").split(",");
                Set<String> colorSet = new HashSet<>(Arrays.asList(colors));

                productDTOList = productDTOList.stream()
                        .filter(productDTO -> productDTO.getProductColorSet().containsAll(colorSet))
                        .collect(Collectors.toList());

                for (var productDTO : productDTOList) {
                    System.out.println(productDTO.getProductID());
                    Set<String> set = productDTO.getProductColorSet();
                    for (var color : set) {
                        System.out.println(color);
                    }
                }
            }

            if (filters.containsKey("size")) {
                String[] sizes = filters.get("size").split(",");
                List<Integer> sizeList = Arrays.stream(sizes).map(Integer::valueOf).collect(Collectors.toList());

                productDTOList = productDTOList.stream()
                        .filter(productDTO -> productDTO.getProductSizeSet().containsAll(sizeList))
                        .collect(Collectors.toList());
            }

            if (filters.containsKey("priceRange")) {
                String[] priceRanges = filters.get("priceRange").split(",");

                productDTOList = productDTOList.stream()
                        .filter(productDTO -> {
                            for (String range : priceRanges) {
                                try {
                                    String[] prices = range.split("-");
                                    BigDecimal minPrice = new BigDecimal(prices[0]);
                                    BigDecimal maxPrice = prices.length > 1 ? new BigDecimal(prices[1]) : BigDecimal.valueOf(Double.MAX_VALUE);

                                    if (productDTO.getProductPriceSet().stream().anyMatch(price -> price.compareTo(minPrice) >= 0 && price.compareTo(maxPrice) <= 0)) {
                                        return true;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid price range: " + range);
                                }
                            }
                            return false;
                        })
                        .collect(Collectors.toList());
            }
        }
        int totalSize = productDTOList.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), totalSize);

        System.out.println("Total size: " + totalSize);
        System.out.println("Start index: " + start);
        System.out.println("End index: " + end);
        System.out.println("Pageable page size: " + pageable.getPageSize());

        List<ProductDTO> pagedProductDTOList = productDTOList.subList(start, end);

        System.out.println("Paged list size: " + pagedProductDTOList.size());

        return new PageImpl<>(pagedProductDTOList, pageable, totalSize);
    }


}