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

执行 `mvn clean package -Dmaven.skip.test=true -Pprod` 即可打包。

打包后会生成在 `target/dist/geekbb` 目录下，上传到服务器解压即可。

1. 服务器需要手动创建一个名为 `db` 的目录，和 `geekbb-latest.jar` 同级
2. 注意修改服务器 `resources/application-prod.properties` 的 Github 和数据库配置

**服务器运行**

```bash
sh tool start
```

## License

[MIT](LICENSE)