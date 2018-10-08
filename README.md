# geekbb

该仓库是 [geek-dev.club](https://geek-dev.club) 的源码，仅供学习参考。

## 使用

<details>
<summary><b>1. 开发环境</b></summary>
<br/>

- JDK8
- MySQL
- Maven
- Lombok

</details>

<details>
<summary><b>2. 创建数据库</b></summary>
<br/>

创建名为 `geekbb` 的数据库，导入 `sql` 目录下的数据库文件。

</details>

<details>
<summary><b>3. 修改配置文件</b></summary>
<br/>

关注 `resources/application.properties` 文件。
修改你的数据库连接和 Github 认证授权信息，[申请 Github APP](https://github.com/settings/applications/new)。

</details>

## 运行

`DEBUG` 模式下运行 `Application` 的主函数即可。 

## 打包

执行 `sh package.sh` 即可打包，也可以自行执行命令。

打包后会生成在 `target/dist/geek-dev-least` 目录下，整个文件夹的内容上传到服务器即可。

1. 服务器需要手动创建一个名为 `db` 的目录，和 `geek-dev-least.jar` 同级
2. 注意修改服务器的 `resources/app.properties` 配置

**服务器运行**

```bash
java -jar -Xms512m -Xmx512m -XX:-LoopUnswitching -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled geek-dev-least.jar
```

这一步也可以自行编写 `shell` 脚本快捷执行。

## License

[MIT](LICENSE)