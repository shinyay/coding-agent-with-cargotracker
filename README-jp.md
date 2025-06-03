# Eclipse Cargo Tracker - Jakarta EE でのドメイン駆動設計の実践的なブループリント

このプロジェクトは、ドメイン駆動設計（DDD）のような広く採用されているアーキテクチャのベストプラクティスを使用して、Jakarta EE でアプリケーションを開発する方法を実証しています。このプロジェクトは、DDD の先駆者である Eric Evans の会社 Domain Language とスウェーデンのソフトウェアコンサルティング会社 Citerus によって開発された有名な [Java DDD サンプルアプリケーション](https://github.com/citerus/dddsample-core) に基づいています。
この貨物の例は、実際に Eric Evans の DDD に関する記念碑的な書籍から来ています。

このアプリケーションは、海運貨物を追跡するためのエンドツーエンドシステムです。以下のセクションで説明されるいくつかのインターフェースがあります。

プロジェクトの詳細については、こちらをご覧ください：https://eclipse-ee4j.github.io/cargotracker/

プロジェクトの基礎を紹介するスライドデッキは、公式 Eclipse Foundation の [Jakarta EE SlideShare アカウント](https://www.slideshare.net/Jakarta_EE/applied-domaindriven-design-blueprints-for-jakarta-ee) で利用できます。スライドデッキの録画は、公式 [Jakarta EE YouTube アカウント](https://www.youtube.com/watch?v=pKmmZd-3mhA) で視聴できます。

![Eclipse Cargo Tracker cover](cargo_tracker_cover.png)

## Java と Jakarta EE について

このプロジェクトを理解するために、Java と Jakarta EE について簡単に説明します：

### Java とは
Java は、Oracle によって開発されたオブジェクト指向プログラミング言語です。「一度書けば、どこでも実行」という哲学のもと、Java 仮想マシン（JVM）上で動作することで、プラットフォームに依存しないアプリケーション開発を可能にします。

### Jakarta EE とは
Jakarta EE（旧 Java EE）は、エンタープライズ Java アプリケーション開発のための仕様とAPIの集合です。以下のような機能を提供します：

- **CDI (Context and Dependency Injection)**: 依存性注入とコンテキスト管理
- **Enterprise Beans**: ビジネスロジックを含むサーバーサイドコンポーネント
- **JPA (Jakarta Persistence API)**: オブジェクト関係マッピング（ORM）
- **JAX-RS**: RESTful ウェブサービス開発
- **Jakarta Faces**: ウェブアプリケーションのユーザーインターフェース構築
- **Jakarta Messaging**: 非同期メッセージング
- **Bean Validation**: データ検証
- **JSON Binding**: JSON とオブジェクト間の変換

## ドメイン駆動設計（DDD）について

### DDD とは何か
ドメイン駆動設計（Domain-Driven Design、DDD）は、Eric Evans によって提唱されたソフトウェア設計手法です。複雑なソフトウェアの中心にあるドメイン（業務領域）とドメインロジックに焦点を当てたアプローチです。

### DDD の主要概念

#### 1. ドメイン（Domain）
アプリケーションが解決しようとする業務上の問題領域です。このプロジェクトでは「海運貨物の追跡」がドメインです。

#### 2. ドメインモデル（Domain Model）
業務の概念、ルール、動作を表現するオブジェクトモデルです。

#### 3. エンティティ（Entity）
一意のアイデンティティを持つオブジェクトです。例：`Cargo`（貨物）、`Location`（場所）

#### 4. 値オブジェクト（Value Object）
アイデンティティを持たず、属性によって定義されるオブジェクトです。例：`TrackingId`（追跡ID）、`UnLocode`（国連ロケーションコード）

#### 5. 集約（Aggregate）
関連するエンティティと値オブジェクトをまとめて整合性を保つ境界です。

#### 6. リポジトリ（Repository）
ドメインオブジェクトの永続化を抽象化するパターンです。

#### 7. ドメインサービス（Domain Service）
エンティティや値オブジェクトに自然に属さないドメインロジックを持つサービスです。

#### 8. アプリケーションサービス（Application Service）
ユースケースを実装し、ドメインオブジェクトを調整する層です。

### このプロジェクトでの DDD の実装

Cargo Tracker では、以下のような DDD パターンが実装されています：

1. **レイヤードアーキテクチャ**: プレゼンテーション層、アプリケーション層、ドメイン層、インフラストラクチャ層に分離
2. **ドメインモデルの分離**: ビジネスロジックをフレームワークから独立させる
3. **ファサードパターン**: 複雑なサブシステムへの簡単なインターフェースを提供

## 始めに

[プロジェクトウェブサイト](https://eclipse-ee4j.github.io/cargotracker/) には、開始方法の詳細情報があります。

最も簡単な手順は以下の通りです（IDE は不要）：

* プロジェクトのソースコードを取得します。
* Java SE 11 または Java SE 17 が動作していることを確認します。
* JAVA_HOME が設定されていることを確認します。
* プロジェクトソースのルートディレクトリに移動し、以下を入力します：
```
./mvnw clean package cargo:run
```
* http://localhost:8080/cargo-tracker にアクセスします

これにより、デフォルトで Payara Server でアプリケーションが実行されます。プロジェクトには GlassFish と Open Liberty をサポートする Maven プロファイルもあります。例えば、以下のコマンドで GlassFish を使用して実行できます：

```
./mvnw clean package -Pglassfish cargo:run
```

同様に、以下のコマンドで Open Liberty を使用して実行できます：

```
./mvnw clean package -Popenliberty liberty:run
```

Visual Studio Code でセットアップするには、以下の手順に従ってください：

* Java SE 11 または Java SE 17、[Visual Studio Code](https://code.visualstudio.com/download)、[Payara 6](https://www.payara.fish/downloads/payara-platform-community-edition/) をセットアップします。また、Visual Studio Code で [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) と [Payara Tools](https://marketplace.visualstudio.com/items?itemName=Payara.payara-vscode) をセットアップする必要があります。
* JAVA_HOME が設定されていることを確認します。
* Visual Studio Code でコードを含むディレクトリを開きます。Visual Studio Code が残りの作業を行い、Maven プロジェクトを自動的に構成するはずです。アプリケーションのクリーン/ビルドを進めてください。
* プロジェクトがビルドされた後（Maven が依存関係をダウンロードするため、初回は時間がかかります）、Payara Tools を使用して `target` ディレクトリ下に生成された `cargo-tracker.war` ファイルを実行するだけです。

Visual Studio Code または Eclipse IDE for Enterprise and Web Developers で GlassFish や Open Liberty を同様に使用できます。

## アプリケーションの探索

アプリケーションが実行されると、http://localhost:8080/cargo-tracker/ で利用できます。内部では、アプリケーションは Faces、CDI、Enterprise Beans、Persistence、REST、Batch、JSON Binding、Bean Validation、Messaging を含む多数の Jakarta EE 機能を使用しています。

いくつかのウェブインターフェース、REST インターフェース、ファイルシステムスキャンインターフェースがあります。以下の大まかな順序でインターフェースを探索することをお勧めします。

追跡インターフェースは貨物のステータスを追跡でき、一般公開用です。ABC123 のような追跡 ID を入力してみてください（アプリケーションにはサンプルデータが事前に入力されています）。

管理インターフェースは貨物を管理する海運会社向けです。インターフェースのランディングページは、登録された貨物の全体的なビューを提供するダッシュボードです。予約インターフェースを使用して貨物を予約できます。
貨物が予約されたら、ルートを設定できます。ルーティングリクエストを開始すると、システムは貨物に適したルートを決定します。ルートを選択すると、貨物は港でのハンドリングイベントを処理する準備が整います。必要に応じて貨物の目的地を変更したり、貨物を追跡したりすることもできます。

ハンドリングイベントロギングインターフェースは、貨物に何が起こったかを登録する港湾職員向けです。インターフェースは主にモバイルデバイス向けですが、デスクトップブラウザーでも使用できます。インターフェースは以下の URL でアクセスできます：
http://localhost:8080/cargo-tracker/event-logger/index.xhtml。便宜上、実際のモバイルデバイスの代わりにモバイルエミュレーターを使用できます。一般的に貨物は以下のイベントを経て処理されます：

* 元の場所で受領される。
* 旅程の航海に積み込み・荷降ろしされる。
* 目的地で引き取られる。
* 任意の地点で税関を通る場合がある。

イベント登録フォームを記入する際は、旅程を手元に置いておくのがベストです。管理インターフェース経由で登録された貨物の旅程にアクセスできます。貨物ハンドリングはスケーラビリティのためにメッセージングを介して行われます。イベントロガーを使用する際は、積み込みと荷降ろしイベントのみが関連する航海を必要とすることに注意してください。

ファイルシステムベースの一括イベント登録インターフェースも探索すべきです。
/tmp/uploads の下のファイルを読み取ります。ファイルは単なる CSV ファイルです。サンプル CSV ファイルは [src/test/sample/handling_events.csv](src/test/sample/handling_events.csv) で利用できます。サンプルは既に貨物 ABC123 の残りの旅程イベントに一致するように設定されています。サンプル CSV ファイルの最初の列の時刻を旅程に合わせて更新するだけです。

正常に処理されたエントリは /tmp/archive の下にアーカイブされます。失敗したレコードは /tmp/failed の下にアーカイブされます。

間違いを心配する必要はありません。アプリケーションはかなりエラー耐性があるように設計されています。問題に遭遇した場合は、[報告](https://github.com/eclipse-ee4j/cargotracker/issues) してください。

./cargo-tracker-data をファイルシステムから削除するだけで、新しく開始できます。このディレクトリは通常、$your-payara-installation/glassfish/domains/domain1/config の下にあります。

ソースコードに含まれる soapUI スクリプトを使用して REST インターフェースを探索したり、コードベース全般をカバーする多数の単体テストを使用したりすることもできます。一部のテストは Arquillian を使用しています。

## コードの探索

前述のように、アプリケーションの真の目的は、適切にアーキテクチャされた効果的な Jakarta EE アプリケーションの作成方法を実証することです。そのため、アプリケーション機能にある程度慣れ親しんだら、次にすべきことはコードを直接掘り下げることです。

DDD はアーキテクチャの重要な側面なので、少なくとも DDD の実用的な理解を得ることが重要です。名前が示すように、ドメイン駆動設計は、コアドメインとドメインロジックに焦点を当てたソフトウェア設計と開発へのアプローチです。

ほとんどの場合、Jakarta EE に新しい場合でも問題ありません。サーバーサイドアプリケーションの基本的な理解があれば、コードは開始するのに十分なはずです。Jakarta EE をさらに学習するために、プロジェクトサイトのリソースセクションでいくつかのリンクを推奨しています。もちろん、プロジェクトの理想的なユーザーは、Jakarta EE と DDD の両方の基本的な実用的理解を持つ人です。Jakarta EE の膨大な API と機能を実証するためのキッチンシンクの例になることが目標ではありませんが、非常に代表的なセットを使用しています。コードを掘り下げて実装方法を確認するだけで、かなりのことを学べるでしょう。

## クラウドデモ
Cargo Tracker は GitHub Actions ワークフローを使用して Kubernetes クラウドにデプロイされています。Scaleforce クラウド（https://cargo-tracker.j.scaleforce.net）でデモデプロイメントを見つけることができます。このプロジェクトは、デモをホストしてくれるスポンサーの [Jelastic](https://jelastic.com) と [Scaleforce](https://www.scaleforce.net) に非常に感謝しています！デプロイメントとすべてのデータは毎晩更新されます。クラウドでは Cargo Tracker はデータベースとして PostgreSQL を使用しています。[GitHub Container Registry](https://ghcr.io/eclipse-ee4j/cargo-tracker) が Docker イメージの公開に使用されています。

## Jakarta EE 8
Cargo Tracker の Jakarta EE 8、Java SE 8、Payara 5 バージョンは ['jakartaee8' ブランチ](https://github.com/eclipse-ee4j/cargotracker/tree/jakartaee8) で利用できます。

## Java EE 7
Cargo Tracker の Java EE 7、Java SE 8、Payara 4.1 バージョンは ['javaee7' ブランチ](https://github.com/eclipse-ee4j/cargotracker/tree/javaee7) で利用できます。

## 貢献

このプロジェクトは [Java](https://google.github.io/styleguide/javaguide.html)、[JavaScript](https://google.github.io/styleguide/jsguide.html)、[HTML/CSS](https://google.github.io/styleguide/htmlcssguide.html) の Google スタイルガイドに準拠しています。
[google-java-format](https://github.com/google/google-java-format) ツールを使用して Google Java スタイルガイドに準拠することができます。Eclipse、Visual Studio Code、IntelliJ などのほとんどの主要な IDE でこのツールを使用できます。

一般的に、すべてのファイルで可能な限り列/行幅を80にし、インデントには2つのスペースを使用します。
すべてのファイルは改行で終わる必要があります。IDE のフォーマット設定を適切に調整してください。HTML Tidy と CSS Tidy を使用してコードをフォーマットすることを推奨しますが、必須ではありません。

プロジェクトロードマップを含む貢献に関するさらなるガイダンスについては、[こちら](CONTRIBUTING.md) をご覧ください。

## 既知の問題

* Visual Studio Code を使用する場合、JAVA_HOME 環境変数が正しく設定されていることを確認してください。適切に構成されていない場合、Visual Studio Code で Payara Server インスタンスを追加する際にドメインを選択できません。
* Visual Studio Code を使用する場合、Payara がスペースを含むパス（例：C:\Program Files\payara6）にインストールされていないことを確認してください。Payara Tools 拡張機能で Payara の起動に失敗します。スペースを含まないパス（例：C:\payara6）に Payara をインストールしてください。
* Payara SSL 証明書が期限切れになったというログメッセージが表示される場合があります。これは機能の妨げにはなりませんが、IDE コンソールにログメッセージが印刷されなくなります。[ここ](https://github.com/payara/Payara/issues/3038) で説明されているように、Payara ドメインから期限切れの証明書を手動で削除することでこの問題を解決できます。
* アプリケーションを数回再起動すると、偽のデプロイメント失敗を引き起こすバグに遭遇します。問題は煩わしいかもしれませんが、害はありません。アプリケーションを再実行してください（アプリケーションを完全にアンデプロイし、最初に Payara をシャットダウンすることを確認してください）。
* サーバーが正しくシャットダウンされなかったり、ロック/権限の問題がある場合、アプリケーションが使用する H2 データベースが破損し、奇妙なデータベースエラーが発生することがあります。これが発生した場合、アプリケーションを停止してデータベースをクリーンアップする必要があります。ファイルシステムから cargo-tracker-data ディレクトリを削除してアプリケーションを再起動するだけでこれを行うことができます。このディレクトリは通常、$your-payara-installation/glassfish/domains/domain1/config の下にあります。
* GlassFish を使用中にテストが `CIRCULAR REFERENCE` エラーで失敗する場合、GlassFish の起動がタイムアウトしたことを意味します。デフォルトのタイムアウトは60秒です。Windows Defender などのウイルススキャナーが GlassFish の起動を遅らせている場合、特に一部のシステムでは十分でない場合があります。`AS_START_TIMEOUT` 環境変数を設定することで GlassFish の起動タイムアウトを増やすことができます。例えば、3分のタイムアウトに対して180000に設定できます。
* Open Liberty で実行中は、多数の偽のエラーが表示されます。shrinkwrap 機能の警告、メッセージ駆動 Bean の警告、AggregateObjectMapping ネストされた外部キーの警告、I/O エラーなどが表示されます。これらは安全に無視できます。アプリケーション機能に影響しません。