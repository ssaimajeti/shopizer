package com.salesmanager.core.compatibility;

import com.salesmanager.core.model.catalog.product.attribute.ProductOptionValue;
import com.salesmanager.core.model.catalog.product.attribute.ProductOptionValueDescription;
import com.salesmanager.core.model.merchant.MerchantStore;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductOptionValueShim extends ProductOptionValue {

    private static final long serialVersionUID = 1L;
    
    @Deprecated
    public MultipartFile getLegacyImage() {
        return super.getImage();
    }
    
    @Deprecated
    public void setLegacyImage(MultipartFile image) {
        super.setImage(image);
    }
    
    public ProductOptionValueShim() {
        // Implement any initialization if necessary
    }

    @Override
    public MultipartFile getImage() {
        return super.getImage();
    }

    @Override
    public void setImage(MultipartFile image) {
        super.setImage(image);
    }

    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    public MerchantStore getMerchantStore() {
        return super.getMerchantStore();
    }

    @Override
    public void setMerchantStore(MerchantStore merchantStore) {
        super.setMerchantStore(merchantStore);
    }

    @Override
    public Integer getProductOptionValueSortOrder() {
        return super.getProductOptionValueSortOrder();
    }

    @Override
    public void setProductOptionValueSortOrder(Integer productOptionValueSortOrder) {
        super.setProductOptionValueSortOrder(productOptionValueSortOrder);
    }

    @Override
    public String getProductOptionValueImage() {
        return super.getProductOptionValueImage();
    }

    @Override
    public void setProductOptionValueImage(String productOptionValueImage) {
        super.setProductOptionValueImage(productOptionValueImage);
    }

    @Override
    public boolean isProductOptionDisplayOnly() {
        return super.isProductOptionDisplayOnly();
    }

    @Override
    public void setProductOptionDisplayOnly(boolean productOptionDisplayOnly) {
        super.setProductOptionDisplayOnly(productOptionDisplayOnly);
    }

    @Override
    public String getCode() {
        return super.getCode();
    }

    @Override
    public void setCode(String code) {
        super.setCode(code);
    }

    @Override
    public Set<ProductOptionValueDescription> getDescriptions() {
        return super.getDescriptions();
    }

    @Override
    public void setDescriptions(Set<ProductOptionValueDescription> descriptions) {
        super.setDescriptions(descriptions);
    }

    @Override
    public List<ProductOptionValueDescription> getDescriptionsList() {
        return super.getDescriptionsList();
    }

    @Override
    public void setDescriptionsList(List<ProductOptionValueDescription> descriptionsList) {
        super.setDescriptionsList(descriptionsList);
    }

    // Ensure this method accurately simulates any logic adjustment during the migration
    public List<ProductOptionValueDescription> getDescriptionsSettoList() {
        return super.getDescriptionsSettoList();
    }
    
    /* 
     * TODO: Any additional manual adjustments related to changes in 
     * Spring MVC Controller endpoints need to be handled manually
     */
}