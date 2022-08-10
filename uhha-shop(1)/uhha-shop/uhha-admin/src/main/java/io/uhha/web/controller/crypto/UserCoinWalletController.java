package io.uhha.web.controller.crypto;

import io.swagger.annotations.Api;
import io.uhha.coin.user.service.IAdminUserCapitalService;
import io.uhha.coin.common.dto.common.Pagination;
import io.uhha.common.constant.Constant;
import io.uhha.coin.user.domain.UserCoinWallet;
import io.uhha.common.core.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(tags = "钱包Controller")
@RestController
@RequestMapping("/crypto/wallet")
public class UserCoinWalletController extends BaseController {
    @Autowired
    private IAdminUserCapitalService adminUserCapitalService;

    // 每页显示多少条数据
    private int numPerPage = Constant.adminPageSize;

    /**
     * 虚拟币钱包
     */
    @GetMapping
    public  Pagination<UserCoinWallet> virtualwalletList(
            @RequestParam(value = "fuids", required = false) String fuids,
            @RequestParam(value = "type", defaultValue = "-1") Integer type,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "keywords", required = false) String keyWord,
            @RequestParam(value = "orderField", defaultValue = "gmt_modified") String orderField,
            @RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection) throws Exception {
        // 搜索关键字
        Pagination<UserCoinWallet> pageParam = new Pagination<UserCoinWallet>(currentPage, numPerPage);
        // 排序条件
        pageParam.setOrderField(orderField);
        pageParam.setOrderDirection(orderDirection);

        UserCoinWallet filterParam = new UserCoinWallet();
        // 关键字
        if (!StringUtils.isEmpty(keyWord)) {
            pageParam.setKeyword(keyWord);
        }
        // 虚拟币类型
        if (type >= 0) {
            filterParam.setCoinId(type);
        }

        List<Integer> fuidsList = null;
        if (fuids != null && !fuids.isEmpty()) {
            try {
                String fuid[] = fuids.split(",");
                fuidsList = new ArrayList<>();
                for (String string : fuid) {
                    fuidsList.add(Integer.valueOf(string));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 查询
        return adminUserCapitalService.selectUserVirtualWalletList(pageParam, filterParam, fuidsList);
    }
}
