'use strict'
const merge = require('webpack-merge')
    ,baseWebpackConfig = require('./webpack.base.conf')
    ,OptimizeCSSPlugin = require('optimize-css-assets-webpack-plugin')
    , shell = require('shelljs');

// const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
// shell.rm('-rf', './dist');

const prodWebpackConfig = merge(baseWebpackConfig, {
  devtool: '',
  plugins: [
    new OptimizeCSSPlugin({
      cssProcessorOptions: {
        safe: true
      }
    }),
    // new BundleAnalyzerPlugin()
  ]
});

module.exports = prodWebpackConfig