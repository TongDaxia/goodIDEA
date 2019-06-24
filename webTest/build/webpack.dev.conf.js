'use strict'
const merge = require('webpack-merge'),
    baseWebpackConfig = require('./webpack.base.conf'),
    webpack = require('webpack'),
    ip= require('ip'),
    AddAssetHtmlPlugin = require('add-asset-html-webpack-plugin');

let proxyObj = {};

['getBondsDefaultProvince', 'getBondsDefaultInformation', 'getRpcData', 'getDictData', 'getDebtData',
    'getProvinceAndCityIncludingSelf', 'getInfoAboutCompanies', 'getFinanceLeasingDetail'
    , 'getFinanceLeasingList', 'getProvinceAndCityIncludingSelf'].forEach(s => {

    proxyObj['/' + s] = {
        // target: 'http://10.15.97.42:8800/finchinaAPP'
        target: 'http://10.15.43.1:8800/finchinaAPP'
    }
});

const devWebpackConfig = merge(baseWebpackConfig, {
    mode: 'development',
    devtool: 'cheap-module-eval-source-map',
    devServer: {
        // host: '10.15.43.69',
        host: /*'localhost'*/ ip.address(),
        disableHostCheck: true,
        // host:"0.0.0.0",
        // port: 8020,
        historyApiFallback: false,
        noInfo: true,
        proxy: {
            '/finchinaAPP': {//在所有请求接口前加/finchinaAPP 即可
                 target: 'http://10.17.234.124:8800/finchinaAPP',//童元刚
                // target: 'http://10.17.234.125:8800/finchinaAPP',//李伟

                // target: 'http://10.17.234.131:8080/finchinaAPP',//张琪
                //target: 'http://10.15.97.42:8800/finchinaAPP',

                // target: 'http://10.15.43.1:8800/finchinaAPP',
                secure: true,  // 如果是https接口，需要配置这个参数
                changeOrigin: true,
                pathRewrite: { '^/finchinaAPP': '' }
            },
        },
        // proxy: proxyObj,
        open: true,
        openPage: 'index.html'
    }
});

module.exports = devWebpackConfig;
