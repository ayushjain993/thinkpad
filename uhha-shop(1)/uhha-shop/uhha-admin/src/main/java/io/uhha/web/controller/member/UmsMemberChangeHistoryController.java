package io.uhha.web.controller.member;

import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.member.domain.UmsMemberChangeHistory;
import io.uhha.member.service.IUmsMemberChangeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户修改历史记录Controller
 *
 * @author peter
 * @date 2021-12-16
 */
@RestController
@RequestMapping("/member/UmsMemberHistory")
public class UmsMemberChangeHistoryController extends BaseController
{
    @Autowired
    private IUmsMemberChangeHistoryService umsMemberChangeHistoryService;

    /**
     * 查询用户修改历史记录列表
     */
    @PreAuthorize("@ss.hasPermi('member:history:list')")
    @GetMapping
    public TableDataInfo list(@RequestParam("mobile") String mobile,
                              @RequestParam("pageSize") Integer pageSize,
                              @RequestParam("pageNum") Integer pageNum)
    {
        startPage();
        List<UmsMemberChangeHistory> list = umsMemberChangeHistoryService.selectUmsMemberChangeHistoryList(mobile);
        return getDataTable(list);
    }

}