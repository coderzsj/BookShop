package cn.ganin.service;

import cn.ganin.common.ServerResponses;
import cn.ganin.pojo.Product;
import cn.ganin.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

/**
 * @Author agamgn
 * @Date 2018-08-06
 **/
public interface IProductService {
    ServerResponses saveOrUpdateProduct(Product product);
    ServerResponses<String> setSaleStatus(Integer productId,Integer status);
    ServerResponses<ProductDetailVo> manageProductDetail(Integer productId);
    ServerResponses<PageInfo> getProductList(int pageNum, int pageSize);
    ServerResponses<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    ServerResponses<ProductDetailVo> getProductDetail(Integer productId);
    ServerResponses<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
