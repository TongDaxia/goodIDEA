'use strict'

const {join, resolve} = require('path'),
	webpack = require('webpack'),
	glob = require('glob'),
	ExtractTextPlugin = require('extract-text-webpack-plugin'),
	HtmlWebpackPlugin = require('html-webpack-plugin'),
	WebpackDeepScopeAnalysisPlugin = require('webpack-deep-scope-plugin').default;

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

const titleMaps = {
	"search":"产品搜索",
	"manager_detail": "投资经理",
	"netValue_table_detail": "净值",
	"notice_detail": "产品公告",
	"product_detail": "产品概况",
	"heavy_stock_detail": "隐形重仓股",
	"related_securities_detail": "管理人管理的产品",
	"separate_bonus_detail": "分红详情",
	"investment_portfolio_detail": "投资组合详情",
	"separate_change_detail": "份额变动详情",
};

    glob.sync('./src/pages/**/app.js').forEach(path => {
	const chunk = path.split('./src/pages/')[1].split('/app.js')[0];
	entries[chunk] = ['babel-polyfill', path];
	chunks.push(chunk);

	htmlWebpackPluginArray.push(new HtmlWebpackPlugin({
		filename: chunk + '.html',
		title: titleMaps[chunk],
		template: 'tpl.ejs',
		inject: 'body',
		hash: true,
		chunks: ['vendors', chunk]
	}))
});

const styleLoaderOptions = {
		loader: 'style-loader',
		options: {
			sourceMap: true
		}
	},
	cssOptions = [
		{loader: 'css-loader', options: {sourceMap: true}},
		{
			loader: 'postcss-loader', options: {
				// sourceMap: true,
				plugins: (loader) => [
					require('autoprefixer')({
						browsers: ['last 10 versions', 'ios 6']
					})
				]
			}
		}
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
			path: resolve(__dirname, '../dest'),
			filename: 'assets/js/[name].js',
			// publicPath: process.env.NODE_ENV === 'production' ? './' : '/'
		},
		resolve: {
			extensions: ['.js', '.vue'], alias: {
				assets: join(__dirname, '../src/assets'),
				components: join(__dirname, '../src/components'),
				'@': join(__dirname, '../src'),
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
							name: 'assets/img/[name].[hash:7].[ext]',
							publicPath: process.env.NODE_ENV === 'production' ? './' : '../../'
							// publicPath: '../../'
						}
					}]
				}
			]
		},
		optimization: {
			splitChunks: {
				cacheGroups: {
					vendors: {
						test: /[\\/]node_modules[\\/]/,
						minChunks: 3,
						minSize: 10000,
						priority: -10,//优先级更高
						name: 'vendors',
						chunks: 'all'
					},
					// commons: {
					//     // chunks: 'initial',
					//     minChunks: 3,
					//     name: 'commons',
					//     enforce: true
					// }
				}
			}
		},
		performance: {
			hints: false
		},
		plugins: [
			new webpack.ProvidePlugin({
				$: "webpack-zepto",
				Zepto: "webpack-zepto"
			}),
			new WebpackDeepScopeAnalysisPlugin(),
			new webpack.optimize.ModuleConcatenationPlugin(),
			extractLESS,
			extractCSS
		]
	};
config.plugins = [...config.plugins, ...htmlWebpackPluginArray];
module.exports = config

