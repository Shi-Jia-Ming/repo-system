package cn.edu.hit.spat.system.controller;

import cn.edu.hit.spat.common.annotation.ControllerEndpoint;
import cn.edu.hit.spat.common.controller.BaseController;
import cn.edu.hit.spat.common.entity.GwarbmsResponse;
import cn.edu.hit.spat.common.entity.QueryRequest;
import cn.edu.hit.spat.common.exception.GwarbmsException;
import cn.edu.hit.spat.system.entity.Orders;
import cn.edu.hit.spat.system.service.IOrdersService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @Author Daijiajia
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("orders")
public class OrdersController extends BaseController {

    private final IOrdersService ordersService;

    @GetMapping("{customername}")
    public Orders getOrders(@NotBlank(message = "{required}") @PathVariable String customername) {
        return this.ordersService.findOrdersDetailList(customername);
    }

    @GetMapping("{ordersId}")
    public Orders getOrders(@NotBlank(message = "{required}") @PathVariable Long ordersId) {
        return this.ordersService.findOrdersDetailList(ordersId);
    }

    @GetMapping("list")
    @RequiresPermissions("orders:view")
    public GwarbmsResponse ordersList(Orders orders, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.ordersService.findOrdersDetailList(orders, request));
        return new GwarbmsResponse().success().data(dataTable);
    }

    @PostMapping
    @RequiresPermissions("orders:create")
    @ControllerEndpoint(operation = "新增订单", exceptionMessage = "新增订单失败")
    public GwarbmsResponse createOrders(@Valid Orders orders) {
        this.ordersService.createOrders(orders);
        return new GwarbmsResponse().success();
    }

    @PostMapping("update")
    @RequiresPermissions("orders:update")
    @ControllerEndpoint(operation = "修改销售单", exceptionMessage = "修改销售单失败")
    public GwarbmsResponse updateOrders(@Valid Orders orders) {
        if (orders.getOrdersId() == null) {
            throw new GwarbmsException("销售单ID为空");
        }
        int msg=this.ordersService.updateOrders(orders);
        if(msg==0)
            return new GwarbmsResponse().fail();
        return new GwarbmsResponse().success();
    }

    @GetMapping("payone/{ordersId}")
    @RequiresPermissions("orders:payone")
    @ControllerEndpoint(operation = "分期付款", exceptionMessage = "本期付款失败")
    public GwarbmsResponse payoneOrders(@NotBlank(message = "{required}") @PathVariable String ordersId) {
        int msg=this.ordersService.payoneOrders(ordersId);
        if(msg==0)
            return new GwarbmsResponse().fail();
        return new GwarbmsResponse().success();
    }

    @GetMapping("submit/{ordersIds}")
    @RequiresPermissions("orders:submit")
    @ControllerEndpoint(operation = "提交订单", exceptionMessage = "提交订单失败")
    public GwarbmsResponse archiveOrders(@NotBlank(message = "{required}") @PathVariable String ordersIds) {
        String[] ids = ordersIds.split(StringPool.COMMA);
        this.ordersService.submitOrders(ids);
        return new GwarbmsResponse().success();
    }

    @GetMapping("audit/{ordersIds}")
    @RequiresPermissions("orders:audit")
    @ControllerEndpoint(operation = "审核订单", exceptionMessage = "审核订单失败")
    public GwarbmsResponse auditOrders(@NotBlank(message = "{required}") @PathVariable String ordersIds) {
        String[] ids = ordersIds.split(StringPool.COMMA);
        this.ordersService.auditOrders(ids);
        return new GwarbmsResponse().success();
    }

    @GetMapping("pay/{ordersIds}")
    @RequiresPermissions("orders:pay")
    @ControllerEndpoint(operation = "收款完毕", exceptionMessage = "订单收款未完成")
    public GwarbmsResponse payOrders(@NotBlank(message = "{required}") @PathVariable String ordersIds) {
        String[] ids = ordersIds.split(StringPool.COMMA);
        int msg = this.ordersService.payOrders(ids);
        if(msg==0)
            return new GwarbmsResponse().fail();
        return new GwarbmsResponse().success();
    }

    @GetMapping("return/{ordersIds}")
    @RequiresPermissions("orders:return")
    @ControllerEndpoint(operation = "订单退货", exceptionMessage = "订单退货未完成")
    public GwarbmsResponse returnOrders(@NotBlank(message = "{required}") @PathVariable String ordersIds) {
        String[] ids = ordersIds.split(StringPool.COMMA);
        this.ordersService.returnOrders(ids);
        return new GwarbmsResponse().success();
    }

    @GetMapping("delete/{ordersIds}")
    @RequiresPermissions("orders:delete")
    @ControllerEndpoint(operation = "删除订单", exceptionMessage = "删除订单失败")
    public GwarbmsResponse deleteOrders(@NotBlank(message = "{required}") @PathVariable String ordersIds) {
        String[] ids = ordersIds.split(StringPool.COMMA);
        this.ordersService.deleteOrders(ids);
        return new GwarbmsResponse().success();
    }
}