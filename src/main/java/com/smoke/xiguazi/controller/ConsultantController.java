package com.smoke.xiguazi.controller;

import com.smoke.xiguazi.model.po.DetectionItem;
import com.smoke.xiguazi.model.vo.DetectionResultFormVo;
import com.smoke.xiguazi.model.vo.DetectionTaskVo;
import com.smoke.xiguazi.model.vo.TransManageFinishedVo;
import com.smoke.xiguazi.model.vo.TransManageVo;
import com.smoke.xiguazi.service.ConsultantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("consultant")
@Slf4j
public class ConsultantController {
    private final ConsultantService consultantService;

    private final List<String> carMakeList;
    private final Map<Integer, DetectionItem> detectionItemMap;


    @Autowired
    public ConsultantController(ConsultantService consultantService,
                                List<String> carMakeList, Map<Integer, DetectionItem> detectionItemMap) {
        this.consultantService = consultantService;

        this.carMakeList = carMakeList;
        this.detectionItemMap = detectionItemMap;
    }

    @ModelAttribute("carMakeList")
    List<String> carMakeList() {
        return carMakeList;
    }

    @ModelAttribute("detectionItemMap")
    Map<Integer, DetectionItem> detectionItemMap() {
        return detectionItemMap;
    }

    /**
     * 重定向到车检车务列表页面
     * @return
     */
    @GetMapping("")
    public String consultantIndex(){
        return "redirect:/consultant/task";
    }

    /**
     * 获取车检任务列表页面
     *
     * @return
     */
    @GetMapping("task")
    public ModelAndView task() {
        //  设置视图
        ModelAndView mav = new ModelAndView("/consultant/task");

        //  获取车检任务
        List<DetectionTaskVo> detectionTaskList = consultantService.getDetectionTaskList();

        //  装配渲染数据
        mav.addObject("detectionTaskList", detectionTaskList);

        return mav;
    }

    /**
     * 管理交易页面
     *
     * @return
     */
    @GetMapping("manage")
    public ModelAndView manage() {
        //  转发视图
        ModelAndView mav = new ModelAndView("consultant/manage");

        //  管理交易列表
        List<TransManageVo> transManageList = consultantService.getTransManageList();

        //  渲染数据
        mav.addObject("transManageList", transManageList);
        return mav;
    }

    @GetMapping("manage/finished")
    public ModelAndView finishedMange(){
        ModelAndView mav = new ModelAndView("consultant/manage-finished");

        //  已完成的交易列表
        List<TransManageFinishedVo> transManageFinishedList = consultantService.getTransManageFinishedList();

        //  渲染数据
        mav.addObject("transManageList", transManageFinishedList);

        return mav;
    }

    /**
     * 获取提交车检结果页面
     */
    @GetMapping("detection-result/add/{transId}")
    public ModelAndView addDetectionResult(@PathVariable("transId") String transId) {
        //  设置视图
        ModelAndView mav = new ModelAndView("/consultant/detection-form");
        DetectionResultFormVo detectionForm = consultantService.getDetectionForm(transId);

        //  装配数据
        mav.addObject("detectionForm", detectionForm);
        return mav;
    }

    /**
     * 处理车检结果表单
     *
     * @param detectionResultForm
     * @return 重定向：上传图片页面
     */
    @PostMapping("post-result")
    public ModelAndView postResult(@ModelAttribute DetectionResultFormVo detectionResultForm) {
        //  重定向到上传图片页面
        ModelAndView mav =
                new ModelAndView("redirect:/consultant/upload-pic/" + detectionResultForm.getTransactionInfo().getTransId());

        //  保存结果
        consultantService.saveDetectionForm(detectionResultForm);

        return mav;
    }

    /**
     * 提交车辆图片页面
     *
     * @param transId
     * @return
     */
    @GetMapping("upload-pic/{transId}")
    public ModelAndView uploadPic(@PathVariable("transId") String transId) {
        //  视图
        ModelAndView mav = new ModelAndView("consultant/upload-pic-form");

        mav.addObject("transId", transId);

        return mav;
    }

    /**
     * 处理上传车辆图片，保存到本地，更新车辆状态，发布
     *
     * @param transId
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("post-picture/{transId}")
    public String savePic(@PathVariable("transId") String transId, HttpServletRequest request) throws IOException {
        //  获取上传的文件对象
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");

        //  业务逻辑
        consultantService.uploadPic(transId, files);

        //  重定向
        return "redirect:/consultant/task";
    }

    /**
     * 取消看车预约
     *
     * @param transId
     * @param bookerId
     * @return
     */
    @GetMapping("reservation/delete")
    public String deleteReservation(@RequestParam("trans_id") String transId,
                                    @RequestParam("booker_id") String bookerId) {

        //  删除预约
        consultantService.deleteReservation(transId, bookerId);

        return "redirect:/consultant/manage";
    }

    /**
     * 确认预约细节, 添加地址信息
     *
     * @param transId
     * @param bookerId
     * @return
     */
    @GetMapping("reservation/confirm")
    public String confirmReservation(@RequestParam("trans_id") String transId,
                                     @RequestParam("booker_id") String bookerId) {
        //  确认交易细节，添加地址
        consultantService.confirmReservation(transId, bookerId);

        return "redirect:/consultant/manage";
    }

    /**
     * 提交交易合同页面
     * @param transId
     * @param bookerId
     * @return
     */
    @GetMapping("upload-contract")
    public ModelAndView uploadContract(@RequestParam("trans_id") String transId,
                               @RequestParam("booker_id") String bookerId) {
        //  视图
        ModelAndView mav = new ModelAndView("consultant/upload-contract-form");

        mav.addObject("transId", transId);
        mav.addObject("bookerId", bookerId);

        return mav;
    }

    /**
     * 处理上传合同文件,更新交易状态
     * @param transId
     * @param bookerId
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("post-contract")
    public String saveContract(@RequestParam("trans_id") String transId,
                               @RequestParam("booker_id") String bookerId, @RequestParam("file") MultipartFile file) throws IOException {
        //  业务逻辑
        consultantService.uploadContract(transId, bookerId, file);

        return "redirect:/consultant/manage";
    }

    /**
     * 确认过户
     * @param transId
     * @return
     */
    @GetMapping("transfer/confirm")
    public String confirmTransfer(@RequestParam("trans_id") String transId){
        //  业务逻辑
        consultantService.confirmTransfer(transId);

        return "redirect:/consultant/manage";
    }
}
