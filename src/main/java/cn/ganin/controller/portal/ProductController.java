package cn.ganin.controller.portal;

import cn.ganin.common.ServerResponses;
import cn.ganin.service.IProductService;
import cn.ganin.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author agamgn
 * @Date 2018-08-06
 **/
@Controller
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    private IProductService iProductService;


    /**
     * 前端详细信息
     * @param productId
     * @return
     */
    @RequestMapping(value = "detail.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses<ProductDetailVo> detail(Integer productId){
        return iProductService.getProductDetail(productId);
    }


    /**
     * 获取前端搜索
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "search.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses<PageInfo> list(@RequestParam(value = "keyword",required = false)String keyword,@RequestParam(value = "categoryId",required = false)Integer categoryId,
                                          @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                          @RequestParam(value = "orderBy",defaultValue = "")String orderBy){

        return iProductService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);


    }
}
