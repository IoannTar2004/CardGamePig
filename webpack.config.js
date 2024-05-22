const path = require("path");
module.exports = {
    watch: true,
    devtool: "source-map",
    entry: {
        start: [
            './src/main/resources/static/js/start/forms.js',
            './src/main/resources/static/js/start/start.js',
            './src/main/resources/static/js/start/startUtils.js',
            './src/main/resources/static/js/utils/requests.js'
        ],
        game: [
            './src/main/resources/static/js/game/distribution.js',
            './src/main/resources/static/js/game/cardManager.js',
            './src/main/resources/static/js/game/events.js',
            './src/main/resources/static/js/game/connection.js',
            './src/main/resources/static/js/game/game.js',
            './src/main/resources/static/js/game/finish.js'
        ],
        home: [
            './src/main/resources/static/js/home/home.js',
            './src/main/resources/static/js/home/header.js',
            './src/main/resources/static/js/home/table.js',
            './src/main/resources/static/js/home/gameRoom.js',
            './src/main/resources/static/js/utils/requests.js',
        ]
    },
    output: {
        path: path.resolve(__dirname, 'src/main/resources/static/js/dist'),
        filename: "[name].js"
    },
    module: {
        rules: [{
            test: /\.js$/,
            exclude: /node_modules/,
            use: {
                loader: "babel-loader",
            },
        },
            {
                test: /\.css$/,
                exclude: /node_modules/,
                use: ["style-loader", "css-loader"]
            }],
    },
    resolve: {
        fallback: {
            "net": false
        }
    }
}