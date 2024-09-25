package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.pojo.UploadTaskRecord;
import com.transAI.service.HundredGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.desktop.SystemSleepEvent;
import java.util.Objects;

@RestController
@RequestMapping("/api/100groups/up10ad")
public class HundredGroupController {

    @Autowired
    private HundredGroupService hundredGroupService;

    @PostMapping("/ch3ck")
    public Result<Boolean> check(@RequestBody UploadTaskRecord record) {
        System.out.println(record);
        if (!Objects.equals(record.admin, "Yosame08")){
            return Result.error("You are not authorized to access this API");
        }
        return Result.success(hundredGroupService.uploadTaskRecord(record));
    }

}
