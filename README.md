# b2bcoin-wallet
B2B Coin Java gui wallet

Can be used for any coin that was created on forknote.

All we need to change are the following files in the resources bundle :

1. configs/application.config
2. configs/coin.conf
3. configs/coin-wallet.conf
4. B2BWallet.java

## configs/application.config
  
Change the following properties:

| Property name | Description   | Example  |
| ------------- |:-------------|:--------|
| coin      | The name of the coin | b2bcoin    |
| wallet-daemon-base-url | The url we start the daemon on (see bind-port in coin-wallet.conf) | http://0.0.0.0:9090 |
| coin-daemon-base-url | The url the coin rpc starts on | http://127.0.0.1:39156 |
| log-file-coin | The name of the coin daemon log file | b2bcoind.log |
| log-file-wallet | The name of the wallet daemon log file | walletd.log |
| min-width | Minimum width of the gui wallet | 1350 |
| min-height | Minimum height of the gui wallet | 775 |
| pool-pools | Comma separated list of pool url that can mine the coin | pool.b2bcoin.ml, pool2.b2bcoin.ml |
| pool-ports | Comma separated list of pool ports for the pools | 3333,5555,7777,9999 |

## configs/coin.conf

The coin.conf file is nothing more then a copy of the configs/%config_file%.conf

## configs/coin-wallet.conf

The coin-wallet.conf file is nothing more then a copy of the configs/%config_file%.conf + some extra properties.

See them here

| Property name | Description   | Example  |
| ------------- |:-------------|:--------|
| daemon-port  | The port of the coin daemon rpc | 39156 |
| bind-port | The port on which we will bind the wllet rpc | 9090 |
| bind-address | This needs to be set somehow for windows (don't change for now and leave the wallet-daemon-base-url in application.config also on 0.0.0.0) | 0.0.0.0 |
| container-file | This will store the containers file location | LEAVE EMPTY (ex. container-file=) |
| container-password | This will store the containers password (this will change in future versions) | LEAVE EMPTY (ex. container-file=) |

## B2BWallet.java

Change the coin name in this line to the cion name chosen in the application.config file. 

* System.setProperty("user.home.forknote", "b2bcoin");


### Your Done !
Building the wallet should get you started with your own coin.


# Customize gui (first phase)

To change the look and feel of the gui wallet you can change some files:

1. my-logo.icns : used by mac to show the app icon.
2. my-logo.ico : icon used by windows to present the app.
3. splash.png : the splash creen background file.
4. B2BUtil.java 

## B2BUtil.java

You can change the 2 main colors of the gui there. (We will extract them and add them to the application.config as well)