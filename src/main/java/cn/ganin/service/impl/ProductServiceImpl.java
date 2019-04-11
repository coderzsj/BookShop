package cn.ganin.service.impl;

import cn.ganin.common.Const;
import cn.ganin.common.ResponseCode;
import cn.ganin.common.ServerResponses;
import cn.ganin.dao.CategoryMapper;
import cn.ganin.dao.ProductMapper;
import cn.ganin.pojo.Category;
import cn.ganin.pojo.Product;

import cn.ganin.service.ICategoryService;
import cn.ganin.service.IProductService;
import cn.ganin.util.DateTimeUtil;
import cn.ganin.util.PropertiesUtil;
import cn.ganin.vo.ProductDetailVo;
import cn.ganin.vo.ProductListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author agamgn
 * @Date 2018-08-06
 **/
@Service("iProductService")
public class ProductServiceImpl implements IProductService {


    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;



    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 保存或者更新
     * @param product
     * @return
     */
    public ServerResponses saveOrUpdateProduct(Product product){
        if (product!=null){
            if (StringUtils.isNotBlank(product.getSubImages())){
                String [] subImageArray=product.getSubImages().split(",");
                if (subImageArray.length>0){
//                    赋值给主图
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId()!=null){
                int rowCount=productMapper.updateByPrimaryKey(product);
                if (rowCount>0){
                    return ServerResponses.createBySuccess("更新成功");
                }
                return ServerResponses.createByErroeMessage("更新产品失败");
            }else{
                int rowCount=productMapper.updateByPrimaryKey(product);
                if (rowCount>0){
                    return ServerResponses.createBySuccess("新增产品成功");
                }
                return ServerResponses.createByErroeMessage("新增产品失败");
            }
        }
        return ServerResponses.createByErroeMessage("新增或更新产品参数不正确");
    }


    /**
     * 更新产品状态，上下架
     * @param productId
     * @param status
     * @return
     */
    public ServerResponses<String> setSaleStatus(Integer productId,Integer status){
        if (productId==null || status==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product=new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount=productMapper.updateByPrimaryKeySelective(product);
        if(rowCount>0){
            return ServerResponses.createBySuccess("修改产品销售状态成功");
        }
        return ServerResponses.createByErroeMessage("修改产品销售状态失败");
    }

    /**
     * 后台获取商品
     * @param productId
     * @return
     */
    public ServerResponses<ProductDetailVo> manageProductDetail(Integer productId){
        if (productId==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponses.createByErroeMessage("产品下架或者删除");
        }

        ProductDetailVo productDetailVo=assembleProductDetailVo(product);
        return ServerResponses.createBySuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo=new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","一个ftp服务"));
        Category category=categoryMapper.selectByPrimaryKey(product.getId());
        if (category==null){
            productDetailVo.setParentCategoryId(0);
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }


    /**
     * 获取商品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponses<PageInfo> getProductList(int pageNum,int pageSize){

        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList=productMapper.selectList();

        List<ProductDetailVo> productDetailVoList= Lists.newArrayList();
        for (Product productItem:productList){
            ProductDetailVo productDetailVo=assembleProductListVo(productItem);
            productDetailVoList.add(productDetailVo);
        }
        PageInfo pageResult=new PageInfo(productList);
        pageResult.setList(productDetailVoList);
        return ServerResponses.createBySuccess(pageResult);
    }

    private ProductDetailVo assembleProductListVo(Product product){
        ProductDetailVo productDetailVo=new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setName(product.getName());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","ftp服务地址"));
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setStatus(product.getStatus());
        return productDetailVo;

    }

    /**
     * 商品搜索
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponses<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        if (StringUtils.isNotBlank(productName)){
            productName=new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList=productMapper.selectByNameAndProductId(productName,productId);

        List<ProductDetailVo> productDetailVoList= Lists.newArrayList();
        for (Product productItem:productList){
            ProductDetailVo productDetailVo=assembleProductListVo(productItem);
            productDetailVoList.add(productDetailVo);
        }
        PageInfo pageResult=new PageInfo(productList);
        pageResult.setList(productDetailVoList);
        return ServerResponses.createBySuccess(pageResult);
    }


    /**
     * 前端获取商品详情
     * @param productId
     * @return
     */
    public ServerResponses<ProductDetailVo> getProductDetail(Integer productId){
        if (productId==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponses.createByErroeMessage("产品下架或者删除");
        }
        if (product.getStatus()!=Const.ProductStatusEnum.ON_SALE.getCode()){
            return ServerResponses.createByErroeMessage("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo=assembleProductDetailVo(product);
        return ServerResponses.createBySuccess(productDetailVo);

    }


    /**
     * 获取前端搜索
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponses<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){

        if (StringUtils.isBlank(keyword)&& categoryId==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList=new ArrayList<Integer>();

        if (categoryId!=null){
            Category category=categoryMapper.selectByPrimaryKey(categoryId);
            if (category==null && StringUtils.isBlank(keyword)){

                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoList=Lists.newArrayList();
                PageInfo pageInfo=new PageInfo(productListVoList);
                return ServerResponses.createBySuccess(pageInfo);
            }
            categoryIdList=iCategoryService.selectCategoryAndChildrenBuId(category.getId()).getData();
        }
        if (StringUtils.isNotBlank(keyword)){
            keyword=new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum,pageSize);
        if (StringUtils.isNotBlank(orderBy)){
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByAray=orderBy.split("_");
                PageHelper.orderBy(orderByAray[0]+""+orderByAray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(keyword,categoryId);
        List<ProductDetailVo> productListVoList= Lists.newArrayList();
        for(Product product : productList){
            ProductDetailVo productDetailVo = assembleProductListVo(product);
            productListVoList.add(productDetailVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponses.createBySuccess(pageInfo);
    }


}

