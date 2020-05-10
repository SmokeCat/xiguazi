package com.smoke.xiguazi.controller;

import com.smoke.xiguazi.model.po.DetectionItem;
import com.smoke.xiguazi.model.vo.CarPageVo;
import com.smoke.xiguazi.model.vo.SearchPageVo;
import com.smoke.xiguazi.model.vo.SellFormVo;
import com.smoke.xiguazi.model.vo.TransSearchParam;
import com.smoke.xiguazi.service.ElasticsearchService;
import com.smoke.xiguazi.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;
    private final ElasticsearchService elasticsearchService;

    Map<String, String> carMakeMap;
    Map<String, String> cityMap;
    Map<String, List<DetectionItem>> detectTypeItemListMap;

    @Autowired
    public TransactionController(TransactionService transactionService, ElasticsearchService elasticsearchService,
                                 Map<String, String> carMakeMap, Map<String, String> cityMap,
                                 @Qualifier("detectTypeItemListMap") Map<String, List<DetectionItem>> detectTypeItemListMap) {
        this.transactionService = transactionService;
        this.elasticsearchService = elasticsearchService;

        this.carMakeMap = carMakeMap;
        this.cityMap = cityMap;
        this.detectTypeItemListMap = detectTypeItemListMap;
    }

    @ModelAttribute("makeMap")
    Map<String, String> makeMap() {
        return carMakeMap;
    }

    @ModelAttribute("cityMap")
    Map<String, String> cityMap() {
        return cityMap;
    }

    @ModelAttribute("detectItemMap")
    Map<String, List<DetectionItem>> detectItemMap() {
        return detectTypeItemListMap;
    }

    /**
     * 车辆搜索页
     *
     * @return 重定向到第一页
     */
    @GetMapping("buy")
    public String buy() {
        return "redirect:/buy/1";
    }

    /**
     * 车辆搜索页，分页
     */
    @GetMapping("buy/{indexPage}")
    public ModelAndView buyWithPage(@PathVariable("indexPage") Long indexPage,
                                    @ModelAttribute TransSearchParam transSearchParam) throws IOException {
        //  转发视图
        ModelAndView mav = new ModelAndView("trans/buy");

        log.debug("transSearchParam:\n" + transSearchParam);

        //  业务逻辑
        SearchPageVo searchPageVO = elasticsearchService.searchTrans(transSearchParam, indexPage);

        //  装配渲染数据
        mav.addObject("searchPageVO", searchPageVO);

        log.debug("searchPageVO indexPage: " + searchPageVO.indexPage() + "totalPage:" + searchPageVO.totalPage());

        return mav;
    }

    /**
     * 车辆详情页面
     *
     * @param transId
     * @return
     */
    @GetMapping("car/{transId}")
    public ModelAndView carPage(@PathVariable("transId") String transId) {
        //  转发视图
        ModelAndView mav = new ModelAndView("trans/car");

        //  业务逻辑
        CarPageVo carPage = transactionService.getCarPage(transId);

        //  装配渲染数据
        mav.addObject("car", carPage);
        log.debug("carPage:\n" + carPage.toString());
        return mav;
    }

    /**
     * 申请卖车页面
     * @return
     */
    @GetMapping("sell")
    public ModelAndView sell(){
        ModelAndView mav = new ModelAndView("trans/sell");
        return mav;
    }

    /**
     * 处理卖车表单
     * @param sellForm
     * @return
     */
    @PostMapping("sell")
    public String sellForm(SellFormVo sellForm){
        //  业务逻辑
        transactionService.sellCar(sellForm);
        //  重定向到主页
        return "redirect:/";
    }

    /**
     * 添加trans到收藏夹中
     *
     * @param transId
     * @return
     */
    @GetMapping("/car/favourite/add/{transId}")
    public String addFavourite(@PathVariable("transId") String transId) {
        //  业务逻辑
        transactionService.addFavourite(transId);

        //  转发页面
        return "redirect:/car/" + transId;
    }

    /**
     * 移除收藏的trans
     *
     * @param transId
     * @return
     */
    @GetMapping("/car/favourite/remove/{transId}")
    public String removeFavourite(@PathVariable("transId") String transId) {
        //  业务逻辑
        transactionService.removeFavourite(transId);

        //  转发页面
        return "redirect:/car/" + transId;
    }

    /**
     * 添加预约
     *
     * @param transId
     * @param meetDateStr
     * @return
     */
    @GetMapping("/reservation/{transId}")
    public String addReservation(@PathVariable("transId") String transId,@ModelAttribute("meetDate") String meetDateStr) {
        //  业务逻辑
        transactionService.addReservation(transId, meetDateStr);

        //  转发页面
        return "redirect:/car/" + transId;
    }
}
