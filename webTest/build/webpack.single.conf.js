'use strict'

const {join, resolve} = require('path'),
    webpack = require('webpack'),
    glob = require('glob'),
    ExtractTextPlugin = require('extract-text-webpack-plugin'),
    HtmlWebpackPlugin = require('html-webpack-plugin');

const entries = {},
    chunks = [],
    htmlWebpackPluginArray = [];

const extractCSS = new ExtractTextPlugin({
        filename: 'assets/css/[name].css',
        allChunks: true
    }),
    extractLESS = new ExtractTextPlugin({
        filename: 'assets/css/[name].css',
        allChunks: true
    });

glob.sync('./src/pages/companyView/seniorExecutive/app.js').forEach(path => {
    const chunk = path.split('./src/pages/')[1].split('/app.js')[0];
    entries[chunk] = path;
    chunks.push(chunk);

    const filename = chunk + '.html',
        htmlConf = {
            filename: filename,
            template: path.replace(/\.js/g, '.html'),
            inject: 'body',
            hash: true,
            // chunks: ['../dll/vendor.dll.js', 'commons', chunk]
            chunks: [ 'commons', chunk]
        };
    htmlWebpackPluginArray.push(new HtmlWebpackPlugin(htmlConf))
});

const styleLoaderOptions = {
        loader: 'style-loader',
        options: {
            sourceMap: true
        }
    },
    cssOptions = [
        {loader: 'css-loader', options: {sourceMap: true}},
        {loader: 'postcss-loader', options: {
            // sourceMap: true,
            plugins: (loader) => [
                require('autoprefixer')({
                    browsers: ['last 10 versions', 'ios 6']
                })
            ]
        }}
    ],
    lessOptions = [...cssOptions, {
        loader: 'less-loader',
        options: {
            sourceMap: true
        }
    }],
    config = {
        entry: entries,
        output: {
            path: resolve(__dirname, '../f9'),
            filename: 'assets/js/[name].js',
            // publicPath: './'
        },
        resolve: {
            extensions: ['.js', '.vue'], alias: {
                assets: join(__dirname, '../src/assets'),
                components: join(__dirname, '../src/components'),
                '@utils': join(__dirname, '../src/utils')
            }
        },
        module: {
            rules: [
                {
                    test: /\.vue$/,
                    loader: 'vue-loader',
                    options: {
                        loaders: {
                            css: ['css-hot-loader'].concat(ExtractTextPlugin.extract({
                                use: cssOptions,
                                fallback: styleLoaderOptions
                            })),
                            less: ['css-hot-loader'].concat(ExtractTextPlugin.extract({
                                use: lessOptions,
                                fallback: styleLoaderOptions
                            })),
                            postcss: ['css-hot-loader'].concat(ExtractTextPlugin.extract({
                                use: cssOptions,
                                fallback: styleLoaderOptions
                            }))
                        }
                    }
                },
                {
                    test: /\.js$/,
                    use: 'babel-loader',
                    exclude: /node_modules/
                },
                {
                    test: /\.css$/,
                    use: ['css-hot-loader'].concat(ExtractTextPlugin.extract({
                        use: cssOptions,
                        fallback: styleLoaderOptions
                    }))
                },
                {
                    test: /\.less$/,
                    use: ['css-hot-loader'].concat(ExtractTextPlugin.extract({
                        use: lessOptions,
                        fallback: styleLoaderOptions
                    }))
                },
                {
                    test: /\.html$/,
                    use: [{
                        loader: 'html-loader',
                        options: {
                            root: resolve(__dirname, 'src'),
                            attrs: ['img:src', 'link:href']
                        }
                    }]
                },
                {
                    test: /\.(png|jpg|jpeg|gif|eot|ttf|woff|woff2|svg|svgz)(\?.+)?$/,
                    exclude: /favicon\.png$/,
                    use: [{
                        loader: 'url-loader',
                        options: {
                            limit: 10000,
                            name: 'assets/img/[name].[hash:7].[ext]'
                        }
                    }]
                }
            ]
        },
        optimization: {
            splitChunks: {
                cacheGroups: {
                    commons: {
                        chunks: 'initial',
                        minChunks: 3,
                        name: 'commons',
                        enforce: true
                    }
                }
            }
        },
        performance: {
            hints: false
        },
        mode: 'development',
        devtool: 'cheap-module-eval-source-map',
        devServer: {
            host: '127.0.0.1',
            // host: '10.15.43.69',
            disableHostCheck: true,
            // host:"0.0.0.0",
            port: 8010,
            historyApiFallback: false,
            noInfo: true,
            proxy: {
                '/api': {
                    target: 'https://appdev.finchina.com/finchinaAPP',
                    changeOrigin: true,
                    // pathRewrite: { '^/api': '' }
                },
                '/getBondsDefaultDynamics': {
                    // target: 'http://appdev.finchina.com/finchinaAPP',
                    target: 'http://10.15.97.42:8800/finchinaAPP',
                    secure: false
                },
                '/getCompanyF9Data': {
                    // target: 'http://appdev.finchina.com/finchinaAPP',
                    target: 'http://10.15.97.42:8800/finchinaAPP',
                    secure: false
                },
                '/getOrgRelationData': {
                    // target: 'http://appdev.finchina.com/finchinaAPP',
                    target: 'http://10.15.97.42:8800/finchinaAPP',
                    secure: false
                },
                '/getRelationData': {
                    // target: 'http://appdev.finchina.com/finchinaAPP',
                    target: 'http://10.15.97.42:8800/finchinaAPP',
                    secure: false
                },
                '/getPersonRelationData': {
                    target: 'http://10.15.97.42:8800/finchinaAPP'
                }
            },
            open: true,
            openPage: 'personInfo.html'
        },
        plugins: [
            new webpack.ProvidePlugin({
                $: "webpack-zepto",
                Zepto: "webpack-zepto"
            }),
            new webpack.optimize.ModuleConcatenationPlugin(),
            new webpack.ContextReplacementPlugin(/moment[\/\\]locale$/, /en/),
            extractLESS,
            extractCSS
        ]
    };
config.plugins = [...config.plugins, ...htmlWebpackPluginArray];






// let arr = ['companyInfo', 'seniorExecutive', 'shareholder', 'changes', 'financialData', 'equityPledge',
//     'pledgeRecord', 'lawsuits', 'courtNotice', 'enforced', 'ncreditArchives', 'taxRate', 'brandInfo',
//     'patentInfo', 'websiteRecord', 'certificate', 'bidInfo'];
//
//
// arr.forEach((v, i) => {
//     devWebpackConfig.devServer.proxy['/' + v] = {
//         target: 'http://appdev.finchina.com/finchinaAPP',
//         secure: false
//     }
// });


// devWebpackConfig.plugins.push(
//     new webpack.DllReferencePlugin({
//         context: __dirname,
//         manifest: require('../dll/vendor-manifest.json')
//     }),
//     new AddAssetHtmlPlugin({filepath: require.resolve('../dll/vendor.dll.js'), includeSourcemap: false})
// );

module.exports = config;
