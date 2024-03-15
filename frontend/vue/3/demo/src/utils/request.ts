import axios, {AxiosInstance, AxiosRequestConfig} from "axios";
import {handleMessage} from "./status";
import { ElMessage } from "element-plus";

interface requestType {
    url: string
    params?: any
}

const service: AxiosInstance = axios.create({
    baseURL:import.meta.env.VITE_APP_BASE_URL,
    timeout:import.meta.env.VITE_APP_TIMEOUT,
});


// 响应拦截
service.interceptors.response.use(
    (response) => {
        return response.data;
    },
    (error) => {
        if (error) {
            return Promise.reject(handleMessage(error.response.data.status));
        } else {
            ElMessage.warning(error);
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

/**
 * @description GET
 */
const GET = ({url,params}: requestType) => {
    return service({
        url,
        method:"GET",
        params
    } as AxiosRequestConfig)
}

/**
 * @description POST
 */
const POST = ({url,params}: requestType) => {
    return service({
        url,
        method:"POST",
        data:params
    } as AxiosRequestConfig)
}
/**
 * @description PUT
 */
const PUT = ({url,params}: requestType) => {
    return service({
        url,
        method:"PUT",
        data:params
    } as AxiosRequestConfig)
}

/**
 * @description DELETE
 */
const DELETE = ({url,params}: requestType) => {
    return service({
        url,
        method: 'DELETE',
        data:params
    } as AxiosRequestConfig)
}

/**
 * @description PATCH
 */
const PATCH = ({url,params}: requestType) => {

    return new Promise((resolve, reject) => {
        service
            .put(url, params)
            .then((res: any) => {
                if (res && res.status == 200) {
                    resolve(res)
                }
            })
            .catch((error: any) => {
                reject(error)
            })
    })
}
export { GET, POST, PUT, DELETE, PATCH }
