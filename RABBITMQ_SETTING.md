# 請按以下步驟安裝程式與設定
# 步驟一: 下載並安裝Erlang
## 1. [前往Erlang官網下載](https://www.erlang.org/downloads)
## 2. 安裝方式:
   ### (1) Windows系統:
   #### a. 點選Download Erlang/OTP區塊右邊的Download Windows installer,根據作業系統選擇64-bit or 32-bit
   #### b. 開啟安裝程式，照預設路徑安裝即可
   ### (2) Linux系統:
   #### a. 點選Download Erlang/OTP區塊右邊的Download source
   #### b. 輸入以下指令解壓縮:
   ```sh
   tar -xvzf otp_src_27.0.tar.gz
   cd otp_src_27.0
   ```
   #### c. 安裝依賴:
   ##### (a) 在Debian/Ubuntu系統:
   ```sh
   sudo apt-get install build-essential libncurses5-dev openssl libssl-dev fop xsltproc unixodbc-dev
   ```
   ##### (b) 在RedHat/CentOS系統:
   ```sh
   sudo yum groupinstall 'Development Tools'
   sudo yum install ncurses-devel openssl-devel wxBase wxGTK wxWidgets unixODBC unixODBC-devel
   ```
   ##### (c) 配置、編譯和安裝:
   ```sh
   ./configure
   make
   sudo make install
   ```

# 步驟二: 下載RabbitMQ
## 1. [前往RabbitMQ官網下載](https://www.rabbitmq.com/docs/download)
## 2. 安裝方式:
   ### (1) Windows系統:
   #### a. 點選側邊欄Install and Upgrade欄位下方的Windows
   #### b. 找到Direct Downloads，點擊下載欄位內的Download下載安裝程式
   #### c. 開啟安裝程式，照預設路徑安裝即可
   #### d. 設定環境變數
   - 若按照預設路徑安裝，在Path新增:
   ```plaintext
   C:\Program Files\RabbitMQ Server\rabbitmq_server-{version}\sbin
   ```
   - 依版本新增，如撰寫這份文件時的版本為3.13.2，ex:
   ```plaintext
   C:\Program Files\RabbitMQ Server\rabbitmq_server-3.13.2\sbin
   ```
   #### e. 輸入以下指令啟用插件
   ##### (a) 啟用管理插件
   ```sh
   rabbitmq-plugins enable rabbitmq_management
   ```
   ##### (b) 啟用Stomp插件
   ```sh
   rabbitmq-plugins enable rabbitmq_stomp
   ```
   ### (2) Linux系統:
   #### a. 點選側邊欄Install and Upgrade欄位下方的Linux/Unix，依個人使用作業環境選擇 Debian/Ubuntu or RedHat
   #### b. 依照官網介紹指令安裝
   #### c. 輸入以下指令啟用插件
   ##### (a) 啟用管理插件
   ```sh
   sudo rabbitmq-plugins enable rabbitmq_management
   ```
   ##### (b) 啟用Stomp插件
   ```sh
   sudo rabbitmq-plugins enable rabbitmq_stomp
   ```
## 3. 管理頁面
  - 安裝完成後，使用步驟三的啟動指令開啟服務後，可訪問管理頁面[管理頁面(預設網址為http://localhost:15672)](http://localhost:15672)
  - 預設帳號:guest
  - 預設密碼:guest

# 步驟三: 啟用RabbitMQ
## 以管理員權限輸入以下指令啟用、設定RabbitMQ
## 1. Windows
### (1) 使用net指令
#### a. 開啟:
```sh
net start RabbitMQ
```
#### b. 關閉:
```sh
net stop RabbitMQ
```
### (2) 使用sc指令
#### a. 開啟:
```sh
sc start RabbitMQ
```
#### b. 關閉:
```sh
sc stop RabbitMQ
```
#### c. 設定開機時自動啟動(預設):
```sh
sc config RabbitMQ start= auto
```
#### d. 關閉開機時自動啟動:
```sh
sc config RabbitMQ start= demand
```
## 2. Linux
### (1) Debian/Ubuntu 系統
#### a. 開啟:
```sh
sudo systemctl start rabbitmq-server
```
#### b. 關閉:
```sh
sudo systemctl stop rabbitmq-server
```
#### c. 重啟:
```sh
sudo systemctl restart rabbitmq-server
```
#### d. 設定開機時自動啟動(預設):
```sh
sudo systemctl enable rabbitmq-server
```
#### e. 關閉開機時自動啟動:
```sh
sudo systemctl disable rabbitmq-server
```
### (2) RedHat/CentOS 系統
#### a. 開啟:
```sh
sudo systemctl start rabbitmq-server
```
#### b. 關閉:
```sh
sudo systemctl stop rabbitmq-server
```
#### c. 重啟:
```sh
sudo systemctl restart rabbitmq-server
```
#### d. 設定開機時自動啟動(預設):
```sh
sudo systemctl enable rabbitmq-server
```
#### e. 關閉開機時自動啟動:
```sh
sudo systemctl disable rabbitmq-server
```
