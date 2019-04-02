package com.xuecheng.framework.domain.cms;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueryBySiteId {
    private String siteId;
    //站点名称
    private String siteName;

}
