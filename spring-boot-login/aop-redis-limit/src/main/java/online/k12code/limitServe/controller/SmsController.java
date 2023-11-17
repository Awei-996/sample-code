package online.k12code.limitServe.controller;

import io.swagger.annotations.ApiOperation;
import online.k12code.limitServe.config.limit.LimitPoint;
import online.k12code.limitServe.utils.VerificationEnums;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Carl
 * @date 2023/8/15
 **/
@RestController
public class SmsController {

    @LimitPoint(name = "sms_send", key = "sms",limit = 1,period = 60)
    @GetMapping("/{verificationEnums}/{mobile}")
    @ApiOperation(value = "发送短信验证码,一分钟同一个ip请求1次")
    public String getSmsCode(
            @PathVariable String mobile,
            @PathVariable VerificationEnums verificationEnums) {
//        verificationService.check(uuid, verificationEnums);
//        smsUtil.sendSmsCode(mobile, verificationEnums, uuid);
        return "s";
    }

    @Async("myTaskAsyncPool")
    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public String login() {
        System.err.println(1);
        // 当前线程名字 http-nio-1101-exec-5
        System.err.println(Thread.currentThread().getName());
        return "s";
    }
}
