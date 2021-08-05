'use strict';

const path                    = require('path');
const webpack                 = require('webpack');
const MiniCssExtractPlugin    = require('mini-css-extract-plugin');
const TSConfigPathsPlugin     = require('tsconfig-paths-webpack-plugin');
const { CleanWebpackPlugin }  = require('clean-webpack-plugin');

const SOURCE_ROOT = __dirname + '/src/main/webpack';

const resolve = {
    extensions: ['.js', '.ts'],
    plugins: [new TSConfigPathsPlugin({
        configFile: './tsconfig.json'
    })]
};

module.exports = {
    resolve: resolve,
    entry: {
        site: SOURCE_ROOT + '/site/main.ts'
    },
    output: {
        path: path.resolve(__dirname, 'dist/clientlib-site')
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                exclude: /node_modules/,
                use: [
                    {
                        options: {
                            eslintPath: require.resolve('eslint'),
                        },
                        loader: require.resolve('eslint-loader'),
                    },
                    {
                        loader: 'ts-loader'
                    },
                    {
                        loader: 'glob-import-loader',
                        options: {
                            resolve: resolve
                        }
                    }
                ]
            },
            {
                test: /\.m?js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                      presets: ['@babel/preset-env']
                    }
                  }
            },
            {
                test: /\.scss$/,
                use: [
                    MiniCssExtractPlugin.loader,
                    {
                        loader: 'css-loader',
                        options: {
                            url: false
                        }
                    },
                    {
                        loader: 'postcss-loader',
                        options: {
                            plugins() {
                                return [
                                    require('autoprefixer')
                                ];
                            }
                        }
                    },
                    {
                        loader: 'sass-loader',
                        options: {
                            url: false
                        }
                    },
                    {
                        loader: 'glob-import-loader',
                        options: {
                            resolve: resolve
                        }
                    }
                ]
            },
            {
                test: /\.(ttf|eot|woff|woff2|svg)$/,
                use: {
                    loader: 'file-loader',
                    options: {
                        name: '[name].[ext]',
                        esModule: false
                    },
                },
            },
            {
              test: /\.hbs$/,
              exclude: /node_modules/,
              loader: "handlebars-loader"
            }
        ]
    },
    plugins: [
        new CleanWebpackPlugin(),
        new MiniCssExtractPlugin({
            filename: '[name].css'
        }),
        new webpack.ProvidePlugin({
          $: 'jquery',
          jQuery: 'jquery',
          'window.jQuery': 'jquery'
        }),
        new webpack.LoaderOptionsPlugin({
          options: {
            handlebarsLoader: {}
          }
        })
    ],
    stats: {
        assetsSort: 'chunks',
        builtAt: true,
        children: false,
        chunkGroups: true,
        chunkOrigins: true,
        colors: false,
        errors: true,
        errorDetails: true,
        env: true,
        modules: false,
        performance: true,
        providedExports: false,
        source: false,
        warnings: true
    },
    resolve: {
      mainFields: ['main', 'module'],
      alias: {
        jquery: path.resolve('../../tetrapak-commons/ui.dev/src/node_modules/jquery'),
        bootstrap: path.resolve('../../tetrapak-commons/ui.dev/src/node_modules/bootstrap'),
        tpPublic: path.resolve('../../tetrapak-publicweb/ui.dev/src/source'),
        tpCommon: path.resolve('../../tetrapak-commons/ui.dev/src/source'),
        'core-js': path.resolve('../../tetrapak-commons/ui.dev/src/node_modules/core-js')
      }
    }
};
