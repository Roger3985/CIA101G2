package com.ren.product.service.impl;

import com.ren.product.dto.ProductDTO;
import com.ren.product.entity.Product;
import com.ren.product.dao.ProductRepository;
import com.ren.product.service.ProductService_interface;
import com.ren.productcategory.dao.ProductCategoryRepository;
import com.ren.productcategory.service.impl.ProductCategoryServiceImpl;
import com.ren.productreview.entity.ProductReview;
import com.ren.productreview.service.impl.ProductReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
     * 評分前10名的商品
     *
     * @return 返回前10名的清單
     */
    @Override
    public List<Product> getTopScore() {
        return productRepository.findTop10ByOrderByProductComScoreDesc();
    }

    /**
     * 最多人評價前10名
     *
     * @return 返回前10名的清單
     */
    @Override
    public List<Product> getTopPopular() {
        return productRepository.findTop10ByOrderByProductComPeopleDesc();
    }

    /**
     * 售出最多前10名
     *
     * @return 返回前10名的清單
     */
    @Override
    public List<Product> getTopSalQty() {
        return productRepository.findTop10ByOrderByProductSalQtyDesc();
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
        return productRepository.findByAttributes(product.getProductNo(), product.getProductCategory().getProductCatNo(), product.getProductName(), product.getProductInfo(), product.getProductSize(), product.getProductColor(), product.getProductPrice(), product.getProductStat(), product.getProductSalQty(), product.getProductComPeople(), product.getProductComScore(), product.getProductOnShelf(), product.getProductOffShelf());
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
     * @param page 指定為第幾頁
     * @param size 指定分頁要呈現多少筆資料
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
            productDTO.setProductList(list);
            HashSet<BigDecimal> productPriceSet = new HashSet<>();
            HashSet<Integer> productSizeSet = new HashSet<>();
            HashSet<String> productColorSet = new HashSet<>();
            HashSet<Timestamp> productOnShelfSet = new HashSet<>();
            Integer productTotalScore = 0;
            Integer productScorePeople = 0;
            Double productScore = 0.0;
            for (Product product : list) {
                System.out.println("productNo: " + product.getProductNo());
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
                    productScorePeople ++;
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
                productDTO.setProductList(list);
                HashSet<BigDecimal> productPriceSet = new HashSet<>();
                HashSet<Integer> productSizeSet = new HashSet<>();
                HashSet<String> productColorSet = new HashSet<>();
                HashSet<Timestamp> productOnShelfSet = new HashSet<>();
                Integer productTotalScore = 0;
                Integer productScorePeople = 0;
                Double productScore = 0.0;
                for (Product product : list) {
                    System.out.println("productNo: " + product.getProductNo());
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
            productDTO.setProductList(list);
            HashSet<BigDecimal> productPriceSet = new HashSet<>();
            HashSet<Integer> productSizeSet = new HashSet<>();
            HashSet<String> productColorSet = new HashSet<>();
            HashSet<Timestamp> productOnShelfSet = new HashSet<>();
            Integer productTotalScore = 0;
            Integer productScorePeople = 0;
            Double productScore = 0.0;
            for (Product product : list) {
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
            productDTO.setProductList(list);
            HashSet<BigDecimal> productPriceSet = new HashSet<>();
            HashSet<Integer> productSizeSet = new HashSet<>();
            HashSet<String> productColorSet = new HashSet<>();
            HashSet<Timestamp> productOnShelfSet = new HashSet<>();
            Integer productTotalScore = 0;
            Integer productScorePeople = 0;
            Double productScore = 0.0;
            for (Product product : list) {
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
        }

        // 前端點擊過濾條件
        if (filters != null) {
            // 根據顏色過濾
            if (filters.containsKey("color")) {
                String[] colors = filters.get("color").split(",");
                productDTOList = productDTOList.stream()
                        .filter(productDTO -> Arrays.stream(colors).anyMatch(color -> productDTO.getProductColorSet().contains(color)))
                        .collect(Collectors.toList());
            }

            // 根據尺寸過濾
            if (filters.containsKey("size")) {
                String[] sizes = filters.get("size").split(",");
                List<Integer> sizeList = Arrays.stream(sizes).map(Integer::valueOf).collect(Collectors.toList());
                productDTOList = productDTOList.stream()
                        .filter(productDTO -> productDTO.getProductSizeSet().stream().anyMatch(sizeList::contains))
                        .collect(Collectors.toList());
            }

            // 根據價格範圍過濾
            if (filters.containsKey("priceRange")) {
                String[] priceRange = filters.get("priceRange").split("-");
                BigDecimal minPrice = new BigDecimal(priceRange[0]);
                BigDecimal maxPrice = new BigDecimal(priceRange[1]);
                productDTOList = productDTOList.stream()
                        .filter(productDTO -> productDTO.getProductPriceSet().stream().anyMatch(price -> price.compareTo(minPrice) >= 0 && price.compareTo(maxPrice) <= 0))
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
            productDTO.setProductList(list);
            HashSet<BigDecimal> productPriceSet = new HashSet<>();
            HashSet<Integer> productSizeSet = new HashSet<>();
            HashSet<String> productColorSet = new HashSet<>();
            HashSet<Timestamp> productOnShelfSet = new HashSet<>();
            Integer productTotalScore = 0;
            Integer productScorePeople = 0;
            Double productScore = 0.0;
            for (Product pro : list) {
                System.out.println("productNo: " + pro.getProductNo());
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
                    productScorePeople ++;
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
        }
        return productDTO;
    }


}