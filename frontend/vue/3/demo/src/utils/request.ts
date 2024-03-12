import axios from "axios";
import { showMessage } from "./status"; // 引入状态码文件
import { ElMessage } from "element-plus"; // 引入el 提示框，这个项目里用什么组件库这里引什么

// 设置接口超时时间
axios.defaults.timeout = 60000;
axios.defaults.baseURL = "/api" || "";  // 自定义接口地址

const token = () => {
    if (sessionStorage.getItem("token")) {
        return sessionStorage.getItem("token");
    } else {
        return sessionStorage.getItem("token");
    }
};

//请求拦截
axios.interceptors.request.use(
    (config) => {
        // 配置请求头
        config.headers["Content-Type"] = "application/json;charset=UTF-8";
        config.headers["token"] = token();
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 响应拦截
axios.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        const { response } = error;
        if (response) {
            // 请求已发出，但是不在2xx的范围
            showMessage(response.status); // 传入响应码，匹配响应码对应信息
            return Promise.reject(response.data);
        } else {
            ElMessage.warning("网络连接异常,请稍后再试!");
        }
    }
);

// 封装 请求并导出
export function request(data: any) {
    return new Promise((resolve, reject) => {
        const promise = axios(data);
        //处理返回
        promise
            .then((res: any) => {
                resolve(res.data);
            })
            .catch((err: any) => {
                reject(err.data);
            });
    });
}
