/*
 * Copyright (c) 2017-2018 THL A29 Limited, a Tencent company. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.abc.matting.tencentcloudapi.iai.v20180301.models;

import com.abc.matting.tencentcloudapi.common.AbstractModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class RevertGroupFaceModelVersionRequest extends AbstractModel{

    /**
    * 需要回滚的升级任务ID。
    */
    @SerializedName("JobId")
    @Expose
    private String JobId;

    /**
     * Get 需要回滚的升级任务ID。 
     * @return JobId 需要回滚的升级任务ID。
     */
    public String getJobId() {
        return this.JobId;
    }

    /**
     * Set 需要回滚的升级任务ID。
     * @param JobId 需要回滚的升级任务ID。
     */
    public void setJobId(String JobId) {
        this.JobId = JobId;
    }

    public RevertGroupFaceModelVersionRequest() {
    }

    /**
     * NOTE: Any ambiguous key set via .set("AnyKey", "value") will be a shallow copy,
     *       and any explicit key, i.e Foo, set via .setFoo("value") will be a deep copy.
     */
    public RevertGroupFaceModelVersionRequest(RevertGroupFaceModelVersionRequest source) {
        if (source.JobId != null) {
            this.JobId = new String(source.JobId);
        }
    }


    /**
     * Internal implementation, normal users should not use it.
     */
    public void toMap(HashMap<String, String> map, String prefix) {
        this.setParamSimple(map, prefix + "JobId", this.JobId);

    }
}

