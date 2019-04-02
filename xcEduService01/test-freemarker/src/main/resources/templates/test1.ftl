<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title>
</head>
<body>
Hello ${name}!
<br/>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
        <td>出生日期</td>
    </tr>

 <#list stus as stu>
     <tr >
         <td>${stu_index+1}</td>
         <td <#if stu.name=='小明'>style="background: chartreuse" </#if> >${(stu.name)}</td>
         <td>${stu.age}</td>
         <td>${stu.money}</td>
         <td>${stu.birthday?string("yyyy年MM月dd日")}}</td>
     </tr>
 </#list>

        <br/>
</table>


    输出stu1的学生信息：<br/>
<#--<#if stuMap?? &&stu1?? >
姓名：${stuMap['stu1'].name}<br/>
年龄：${stuMap['stu1'].age}<br/>
输出stu1的学生信息第二章写法：<br/>
</#if>-->
姓名：${(stuMap.stu1.name)!'po1'}<br/>
年龄：${(stuMap.stu1.age)!'po2'}<br/>
    遍历stuMap的key：推荐<br/>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr><br/>
    <#list stuMap?keys as k>
        <tr>
        <td>${k_index+1}</td>
        <td>${stuMap[k].name}</td>
        <td <#if (stuMap[k].age>18)>style="background: red" </#if>>${stuMap[k].age}</td>
        <td>${stuMap[k].money}</td>
        </tr>
    </#list>

</table>

输出stu1的学生信息：<br/>
<#if stu1??>
姓名：${stu1.name}<br/>
年龄：${stu1.age}<br/>
</#if>

显示stu2的最好朋友<br/>
${stuMap.stu2.bestFriend}<br/>

显示stu2的所有朋友<br/>

<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr><br/>
<#list friends as fr>
    <tr >
        <td>${fr_index+1}</td>
        <td  >${(fr.name)}</td>
        <td>${fr.age}</td>
        <td>${fr.money}</td>
    </tr>
</#list>

</table><br/>
显示长的数字：<br/>
${point?c}<br/>

关于assign变量<br/>
<#assign text="{'bank':'工商银行','account':'10101920201920212'}" />
<#assign data=text?eval /><br/>
开户行：${data.bank} <br/>
账号：${data.account}
</body>
</html>