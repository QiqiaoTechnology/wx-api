package com.zhggp.qiqiao.wx.controller;

import com.zhggp.qiqiao.wx.common.utils.R;
import com.zhggp.qiqiao.wx.db.dto.MaterialNewsDto;
import com.zhggp.qiqiao.wx.db.dto.WxMpInfo;
import com.zhggp.qiqiao.wx.db.form.MaterialFileDeleteForm;
import com.zhggp.qiqiao.wx.service.WxAssetsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.material.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 微信公众号素材管理
 * 参考官方文档：https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/New_temporary_materials.html
 * 参考WxJava开发文档：https://github.com/Wechat-Group/WxJava/wiki/MP_永久素材管理
 */
@RestController
@RequestMapping("/manage/wxAssets")
@Api(tags = {"a岑璐要的接口-公众号素材-管理后台"})
public class WxAssetsManageController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    WxAssetsService wxAssetsService;
    @Autowired
    WxMpInfo wxMpInfo;

    /**
     * 获取素材总数
     *
     * @return
     * @throws WxErrorException
     */
    @GetMapping("/materialCount")
    @ApiOperation(value = "文件素材总数")
    public R materialCount() throws WxErrorException {
        String appid = wxMpInfo.getAppId();
        logger.info("WxAssetsManageController.materialCount appid={}", appid);
        WxMpMaterialCountResult res = wxAssetsService.materialCount(appid);
        return R.ok().put(res);
    }

    /**
     * 获取素材总数
     *
     * @return
     * @throws WxErrorException
     */
    @GetMapping("/materialNewsInfo")
    @ApiOperation(value = "图文素材总数")
    public R materialNewsInfo(String mediaId) throws WxErrorException {
        String appid = wxMpInfo.getAppId();

        WxMpMaterialNews res = wxAssetsService.materialNewsInfo(appid, mediaId);
        return R.ok().put(res);
    }


    /**
     * 根据类别分页获取非图文素材列表
     *
     * @param type
     * @param page
     * @return
     * @throws WxErrorException
     */
    @GetMapping("/materialFileBatchGet")
    //@RequiresPermissions("wx:wxassets:list")
    @ApiOperation(value = "根据类别分页获取非图文素材列表")
    public R materialFileBatchGet(@RequestParam(defaultValue = "image") String type,
                                  @RequestParam(defaultValue = "1") int page) throws WxErrorException {
        String appid = wxMpInfo.getAppId();

        WxMpMaterialFileBatchGetResult res = wxAssetsService.materialFileBatchGet(appid, type, page);
        return R.ok().put(res);
    }

    /**
     * 分页获取图文素材列表
     *
     * @param page
     * @return
     * @throws WxErrorException
     */
    @GetMapping("/materialNewsBatchGet")
    //@RequiresPermissions("wx:wxassets:list")
    @ApiOperation(value = "分页获取图文素材列表")
    public R materialNewsBatchGet(@RequestParam(defaultValue = "1") int page) throws WxErrorException {
        String appid = wxMpInfo.getAppId();

        WxMpMaterialNewsBatchGetResult res = wxAssetsService.materialNewsBatchGet(appid, page);
        MaterialNewsDto result = new MaterialNewsDto();
        result.setTotalCount(res.getTotalCount());
        result.setItemCount(res.getItemCount());
        List<HashMap> mapList = new ArrayList<>();
        for (WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem item : res.getItems()) {
            HashMap map = new HashMap();
            map.put("mediaId", item.getMediaId());
            map.put("updateTime", item.getMediaId());
            map.put("thumbMediaId", item.getContent().getArticles().get(0).getThumbMediaId());

            map.put("thumbUrl", item.getContent().getArticles().get(0).getThumbUrl());
            map.put("author", item.getContent().getArticles().get(0).getAuthor());
            map.put("title", item.getContent().getArticles().get(0).getTitle());
            map.put("contentSourceUrl", item.getContent().getArticles().get(0).getContentSourceUrl());
            map.put("content", item.getContent().getArticles().get(0).getContent());
            map.put("digest", item.getContent().getArticles().get(0).getDigest());
            map.put("showCoverPic", item.getContent().getArticles().get(0).isShowCoverPic());
            map.put("url", item.getContent().getArticles().get(0).getUrl());
            map.put("needOpenComment", item.getContent().getArticles().get(0).getNeedOpenComment());
            map.put("onlyFansCanComment", item.getContent().getArticles().get(0).getOnlyFansCanComment());

            mapList.add(map);
        }
        result.setItems(mapList);
        return R.ok().put(result);
    }

    /**
     * 添加图文永久素材
     *
     * @param articles
     * @return
     * @throws WxErrorException
     */
    @PostMapping("/materialNewsUpload")
    //@RequiresPermissions("wx:wxassets:save")
    @ApiOperation(value = "添加图文永久素材")
    public R materialNewsUpload(@RequestBody List<WxMpNewsArticle> articles) throws WxErrorException {
        if (articles.isEmpty()) {
            return R.error("图文列表不得为空");
        }
        String appid = wxMpInfo.getAppId();
        WxMpMaterialUploadResult res = wxAssetsService.materialNewsUpload(appid, articles);
        return R.ok().put(res);
    }

    /**
     * 修改图文素材文章
     *
     * @param form
     * @return
     * @throws WxErrorException
     */
    @PostMapping("/materialArticleUpdate")
    //@RequiresPermissions("wx:wxassets:save")
    @ApiOperation(value = "修改图文素材文章")
    public R materialArticleUpdate(@RequestBody WxMpMaterialArticleUpdate form) throws WxErrorException {
        if (form.getArticles() == null) {
            return R.error("文章不得为空");
        }
        String appid = wxMpInfo.getAppId();
        wxAssetsService.materialArticleUpdate(appid, form);
        return R.ok();
    }

    /**
     * 添加多媒体永久素材
     *
     * @param file
     * @param fileName
     * @param mediaType
     * @return
     * @throws WxErrorException
     * @throws IOException
     */
    @PostMapping("/materialFileUpload")
    //@RequiresPermissions("wx:wxassets:save")
    @ApiOperation(value = "添加多媒体永久素材")
    public R materialFileUpload(MultipartFile file, String fileName, String mediaType) throws WxErrorException, IOException {
        if (file == null) {
            return R.error("文件不得为空");
        }
        String appid = wxMpInfo.getAppId();
        WxMpMaterialUploadResult res = wxAssetsService.materialFileUpload(appid, mediaType, fileName, file);
        return R.ok().put(res);
    }

    /**
     * 删除素材
     *
     * @param form
     * @return
     * @throws WxErrorException
     */
    @PostMapping("/materialDelete")
    //@RequiresPermissions("wx:wxassets:delete")
    @ApiOperation(value = "删除素材")
    public R materialDelete(@RequestBody MaterialFileDeleteForm form) throws WxErrorException {
        String appid = wxMpInfo.getAppId();
        boolean res = wxAssetsService.materialDelete(appid, form.getMediaId());
        return R.ok().put(res);
    }

}
