# Eclipse Cargo Tracker - Jakarta EE のための実用的なドメイン駆動設計の設計図

このプロジェクトは、ドメイン駆動設計（DDD）などの広く採用されているアーキテクチャのベストプラクティスを使用して、Jakarta EE でアプリケーションを開発する方法を実証しています。このプロジェクトは、DDDの先駆者であるエリック・エヴァンス氏の会社Domain Languageとスウェーデンのソフトウェアコンサルティング会社Citerusによって開発された、よく知られた[Java DDD サンプルアプリケーション](https://github.com/citerus/dddsample-core)に基づいています。カーゴの例は、実際にエリック・エヴァンス氏のDDDに関する有名な書籍から来ています。

このアプリケーションは、輸送貨物を追跡するためのエンドツーエンドシステムです。以下のセクションで説明されているいくつかのインターフェースがあります。

プロジェクトの詳細については、https://eclipse-ee4j.github.io/cargotracker/ をご覧ください。

プロジェクトの基礎を紹介するスライドデッキは、公式Eclipse Foundation [Jakarta EE SlideShare アカウント](https://www.slideshare.net/Jakarta_EE/applied-domaindriven-design-blueprints-for-jakarta-ee)で利用できます。スライドデッキの録画は、公式[Jakarta EE YouTube アカウント](https://www.youtube.com/watch?v=pKmmZd-3mhA)で視聴できます。

![Eclipse Cargo Tracker cover](cargo_tracker_cover.png)

## Java技術について

Javaを初めて学ぶ方、またはJavaにあまり詳しくない方のために、このプロジェクトで使用されている主要なJava技術について説明します。

### Java とは

Java は、1995年にSun Microsystems（現在はOracle）によって開発されたプログラミング言語およびプラットフォームです。Javaの主な特徴は以下の通りです：

- **プラットフォーム独立性**: "一度書けば、どこでも実行できる"（Write Once, Run Anywhere）
- **オブジェクト指向**: クラスとオブジェクトを基盤とした設計
- **メモリ管理**: ガベージコレクションによる自動メモリ管理
- **強力な型システム**: コンパイル時のエラー検出

### Jakarta EE とは

Jakarta EE（旧Java EE）は、エンタープライズアプリケーション開発のためのJavaプラットフォームです。以下の機能を提供します：

- **Web アプリケーション**: ブラウザベースのユーザーインターフェース
- **RESTful サービス**: HTTP ベースのAPI
- **データベース連携**: 永続化とトランザクション管理
- **メッセージング**: 非同期通信
- **セキュリティ**: 認証と認可
- **依存性注入**: コンポーネント間の疎結合

### このプロジェクトで使用される主要技術

1. **CDI (Contexts and Dependency Injection)**: オブジェクトの生成と管理
2. **JPA (Java Persistence API)**: データベースとのやりとり
3. **JAX-RS**: REST API の作成
4. **JSF (JavaServer Faces)**: Webユーザーインターフェース
5. **Bean Validation**: データ検証
6. **JMS (Java Message Service)**: 非同期メッセージング

## ドメイン駆動設計（DDD）について

### DDD とは何か

ドメイン駆動設計（Domain-Driven Design, DDD）は、エリック・エヴァンス氏によって提唱されたソフトウェア設計手法です。複雑なビジネス問題を解決するためのアプローチで、以下の原則に基づいています：

### DDD の核となる概念

#### 1. ドメイン（Domain）
- **定義**: 解決したいビジネス上の問題領域
- **例**: このプロジェクトでは「貨物輸送管理」がドメイン

#### 2. ユビキタス言語（Ubiquitous Language）
- **定義**: 開発者とドメインエキスパートが共通して使用する言語
- **重要性**: コミュニケーションエラーを減らし、コードとビジネスの整合性を保つ
- **例**: 「貨物（Cargo）」「航海（Voyage）」「積み込み（Load）」

#### 3. エンティティ（Entity）
- **定義**: 一意の識別子を持つオブジェクト
- **特徴**: ライフサイクル全体を通じて同一性を保つ
- **例**: Cargo（一意の追跡ID を持つ）

#### 4. 値オブジェクト（Value Object）
- **定義**: 属性の値のみで識別されるオブジェクト
- **特徴**: 不変（immutable）である
- **例**: Location、UnLocode

#### 5. 集約（Aggregate）
- **定義**: 関連するエンティティと値オブジェクトのクラスタ
- **目的**: データの一貫性境界を定義
- **例**: Cargo 集約（CargoクラスとItineraryクラスなど）

#### 6. 集約ルート（Aggregate Root）
- **定義**: 集約へのエントリーポイント
- **役割**: 外部からの集約内オブジェクトへのアクセスを制御
- **例**: Cargo クラス

#### 7. リポジトリ（Repository）
- **定義**: ドメインオブジェクトのコレクションのような抽象化
- **目的**: データアクセスロジックをドメインロジックから分離
- **例**: CargoRepository

#### 8. ドメインサービス（Domain Service）
- **定義**: エンティティや値オブジェクトに自然に属さないビジネスロジック
- **例**: RoutingService（経路計算サービス）

### DDD のアーキテクチャ層

#### 1. ユーザーインターフェース層
- **役割**: ユーザーからの入力を受け取り、情報を表示
- **技術**: JSF, REST API

#### 2. アプリケーション層
- **役割**: ユースケースを調整し、ドメインオブジェクトを協調させる
- **特徴**: ビジネスロジックは含まない
- **例**: BookingService

#### 3. ドメイン層
- **役割**: ビジネスコンセプトとルールを表現
- **中核**: DDD の心臓部分
- **例**: Cargo, Voyage, Handling

#### 4. インフラストラクチャ層
- **役割**: 上位層の技術的機能をサポート
- **例**: データベースアクセス, 外部サービス連携

### このプロジェクトでの DDD 実装

Cargo Tracker プロジェクトは、以下のようにDDDパターンを実装しています：

1. **境界づけられたコンテキスト**:
   - Booking（予約）
   - Tracking（追跡）
   - Handling（取扱）

2. **主要な集約**:
   - Cargo: 貨物とその行程
   - Voyage: 船の航海
   - HandlingEvent: 取扱イベント

3. **ドメインサービス**:
   - RoutingService: 最適な輸送経路を計算
   - BookingService: 貨物予約を管理

### DDD の利点

1. **ビジネス価値の最大化**: 複雑なビジネス問題に焦点を当てる
2. **保守性の向上**: ビジネスロジックが明確に分離されている
3. **チームコミュニケーション**: 共通言語による意思疎通
4. **変更への対応**: ビジネス要件の変更に柔軟に対応

## 開始方法

[プロジェクトウェブサイト](https://eclipse-ee4j.github.io/cargotracker/)には、開始方法の詳細な情報があります。

最もシンプルな手順は以下の通りです（IDEは不要）：

* プロジェクトのソースコードを取得します
* Java SE 11 または Java SE 17 を実行していることを確認します
* JAVA_HOME が設定されていることを確認します
* プロジェクトソースのルートに移動し、以下をタイプします：
```
./mvnw clean package cargo:run
```
* http://localhost:8080/cargo-tracker にアクセスします

これにより、デフォルトでPayara Serverを使用してアプリケーションが実行されます。プロジェクトには、GlassFishとOpen Libertyをサポートするためのmavenプロファイルもあります。例えば、以下のコマンドを使用してGlassFishで実行できます：

```
./mvnw clean package -Pglassfish cargo:run
```

同様に、以下のコマンドを使用してOpen Libertyで実行できます：

```
./mvnw clean package -Popenliberty liberty:run
```

Visual Studio Codeでセットアップするには、以下の手順に従ってください：

* Java SE 11、または Java SE 17、[Visual Studio Code](https://code.visualstudio.com/download)、および[Payara 6](https://www.payara.fish/downloads/payara-platform-community-edition/)をセットアップします。また、Visual Studio Code で[Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)と[Payara Tools](https://marketplace.visualstudio.com/items?itemName=Payara.payara-vscode)をセットアップする必要があります
* JAVA_HOME が設定されていることを確認します
* Visual Studio Code でコードを含むディレクトリを開きます。Visual Studio Code が残りの作業を行い、自動的にmavenプロジェクトを設定します。アプリケーションのクリーン/ビルドを続行します
* プロジェクトがビルドされた後（依存関係をmavenがダウンロードするため、初回は時間がかかります）、Payara Tools を使用して`target`ディレクトリ下に生成された`cargo-tracker.war`ファイルを実行するだけです

Visual Studio Code または Eclipse IDE for Enterprise and Web Developers で同様にGlassFish または Open Liberty を使用できます。

## アプリケーションの探索

アプリケーションが実行されると、http://localhost:8080/cargo-tracker/ で利用できます。内部では、アプリケーションはFaces、CDI、Enterprise Beans、Persistence、REST、Batch、JSON Binding、Bean Validation、Messagingを含む多数のJakarta EE機能を使用しています。

いくつかのWebインターフェース、RESTインターフェース、およびファイルシステムスキャンインターフェースがあります。以下の大まかな順序でインターフェースの探索を開始することをお勧めします。

追跡インターフェースは、貨物の状態を追跡することができ、一般向けです。ABC123のような追跡IDを入力してみてください（アプリケーションにはいくつかのサンプルデータが事前に入力されています）。

管理インターフェースは、貨物を管理する輸送会社向けです。インターフェースのランディングページは、登録された貨物の全体的な概要を提供するダッシュボードです。予約インターフェースを使用して貨物を予約できます。貨物が予約されると、ルートを設定できます。ルーティングリクエストを開始すると、システムは貨物に適用可能なルートを決定します。ルートを選択すると、貨物は港でのハンドリングイベントを処理する準備が整います。必要に応じて貨物の目的地を変更したり、貨物を追跡することもできます。

ハンドリングイベントログインターフェースは、貨物に何が起こったかを登録する港湾職員向けです。インターフェースは主にモバイルデバイス向けですが、デスクトップブラウザーを使用して使用できます。インターフェースは次のURLでアクセスできます：http://localhost:8080/cargo-tracker/event-logger/index.xhtml。便宜上、実際のモバイルデバイスの代わりにモバイルエミュレーターを使用できます。一般的に言えば、貨物は以下のイベントを通過します：

* 出発地で受領される
* 行程表の航海に積み込み・荷降ろしされる
* 目的地で請求される
* 任意の地点で税関を通過する場合がある

イベント登録フォームの記入中は、行程表を手元に置いておくことが最善です。管理インターフェースを通じて、登録された貨物の行程表にアクセスできます。貨物処理は、スケーラビリティのためにメッセージングを介して行われます。イベントロガーを使用する際は、積み込みと荷降ろしイベントのみが関連する航海を必要とすることに注意してください。

ファイルシステムベースのバルクイベント登録インターフェースも探索する必要があります。これは/tmp/uploads下のファイルを読み取ります。ファイルはCSVファイルです。サンプルCSVファイルは [src/test/sample/handling_events.csv](src/test/sample/handling_events.csv) で利用できます。サンプルは、貨物ABC123の残りの行程表イベントに一致するように既に設定されています。サンプルCSVファイルの最初の列の時間を行程表に合わせて更新するだけです。

正常に処理されたエントリは/tmp/archiveの下にアーカイブされます。失敗したレコードは/tmp/failedの下にアーカイブされます。

間違いを犯すことを心配する必要はありません。アプリケーションはかなりエラートレラントになるように意図されています。問題に遭遇した場合は、[報告](https://github.com/eclipse-ee4j/cargotracker/issues)してください。

新しく開始するには、ファイルシステムから./cargo-tracker-dataを削除するだけです。このディレクトリは通常$your-payara-installation/glassfish/domains/domain1/config下にあります。

ソースコードに含まれているsoapUIスクリプトを使用してRESTインターフェースを探索したり、コードベース全般をカバーする多数の単体テストを使用することもできます。一部のテストはArquillianを使用しています。

## コードの探索

前述したように、アプリケーションの本当のポイントは、よく設計された効果的なJakarta EEアプリケーションの作成方法を実証することです。そのため、アプリケーション機能にある程度慣れたら、次にすべきことはコードを詳しく調べることです。

DDDはアーキテクチャの重要な側面であるため、少なくともDDDの実用的な理解を得ることが重要です。名前が示すように、ドメイン駆動設計は、コアドメインとドメインロジックに焦点を当てたソフトウェア設計と開発へのアプローチです。

大部分において、Jakarta EEが初めてでも問題ありません。サーバーサイドアプリケーションの基本的な理解があれば、コードは開始するのに十分なはずです。Jakarta EEをさらに学習するために、プロジェクトサイトのリソースセクションでいくつかのリンクを推奨しています。もちろん、プロジェクトの理想的なユーザーは、Jakarta EEとDDDの両方の基本的な実用的理解を持つ人です。Jakarta EEの膨大な数のAPIと機能を実証するためのキッチンシンクサンプルになることは私たちの目標ではありませんが、非常に代表的なセットを使用しています。コードを詳しく調べて物事がどのように実装されているかを見ることで、かなりの量を学ぶことができるでしょう。

## クラウドデモ

Cargo TrackerはGitHub Actionsワークフローを使用してクラウド上のKubernetesにデプロイされています。Scaleforceクラウド（https://cargo-tracker.j.scaleforce.net）でデモデプロイメントを見つけることができます。このプロジェクトは、デモをホスティングしてくれるスポンサーの[Jelastic](https://jelastic.com)と[Scaleforce](https://www.scaleforce.net)に非常に感謝しています！デプロイメントとすべてのデータは毎晩更新されます。クラウド上では、Cargo TrackerはデータベースとしてPostgreSQLを使用しています。[GitHub Container Registry](https://ghcr.io/eclipse-ee4j/cargo-tracker)を使用してDockerイメージを公開しています。

## Jakarta EE 8

Cargo TrackerのJakarta EE 8、Java SE 8、Payara 5版は['jakartaee8'ブランチ](https://github.com/eclipse-ee4j/cargotracker/tree/jakartaee8)で利用できます。

## Java EE 7

Cargo TrackerのJava EE 7、Java SE 8、Payara 4.1版は['javaee7'ブランチ](https://github.com/eclipse-ee4j/cargotracker/tree/javaee7)で利用できます。

## 貢献

このプロジェクトは、[Java](https://google.github.io/styleguide/javaguide.html)、[JavaScript](https://google.github.io/styleguide/jsguide.html)、および[HTML/CSS](https://google.github.io/styleguide/htmlcssguide.html)のGoogleスタイルガイドに準拠しています。[google-java-format](https://github.com/google/google-java-format)ツールを使用して、Google Javaスタイルガイドに準拠することができます。Eclipse、Visual Studio Code、IntelliJなどの主要なIDEでツールを使用できます。

一般的に、すべてのファイルで可能な限り80の列/行幅を使用し、インデントには2つのスペースを使用します。すべてのファイルは改行で終わる必要があります。それに応じてIDEのフォーマット設定を調整してください。HTML TidyやCSS Tidyを使用してコードをフォーマットすることを推奨しますが、必須ではありません。

プロジェクトロードマップを含む貢献に関する詳細なガイダンスについては、[こちら](CONTRIBUTING.md)をご覧ください。

## 既知の問題

* Visual Studio Codeを使用する場合、JAVA_HOME環境変数が正しく設定されていることを確認してください。適切に設定されていない場合、Visual Studio CodeでPayara Serverインスタンスを追加する際にドメインを選択できません。
* Visual Studio Codeを使用する場合、Payaraがスペースのあるパス（例：C:\Program Files\payara6）にインストールされていないことを確認してください。PayaraはPayara Tools拡張機能での起動に失敗します。スペースのないパス（例：C:\payara6）にPayaraをインストールしてください。
* Payara SSL証明書の有効期限が切れているというログメッセージが表示される場合があります。これは機能に支障をきたしませんが、ログメッセージがIDEコンソールに印刷されるのを停止します。[こちら](https://github.com/payara/Payara/issues/3038)で説明されているように、Payaraドメインから期限切れの証明書を手動で削除することで、この問題を解決できます。
* アプリケーションを数回再起動すると、偽のデプロイメント失敗を引き起こすバグに遭遇します。この問題は迷惑ですが、無害です。アプリケーションを再実行してください（アプリケーションを完全にアンデプロイして、最初にPayaraをシャットダウンしてください）。
* サーバーが正しくシャットダウンされなかった場合、またはロック/権限の問題がある場合、アプリケーションが使用するH2データベースが破損し、奇妙なデータベースエラーが発生することがあります。これが発生した場合、アプリケーションを停止してデータベースをクリーンにする必要があります。ファイルシステムからcargo-tracker-dataディレクトリを削除してアプリケーションを再起動するだけでこれを行うことができます。このディレクトリは通常$your-payara-installation/glassfish/domains/domain1/config下にあります。
* GlassFishを使用している場合、テストが`CIRCULAR REFERENCE`エラーで失敗する場合、GlassFishの起動がタイムアウトしたことを意味します。デフォルトのタイムアウトは60秒です。これは、特にWindows DefenderなどのウイルススキャナーがGlassFishの起動を遅らせている場合、一部のシステムでは十分でない場合があります。`AS_START_TIMEOUT`環境変数を設定することで、GlassFishの起動タイムアウトを増やすことができます。例えば、3分のタイムアウトの場合、180000に設定できます。
* Open Libertyで実行中、多数の偽のエラーが表示されます。shrinkwrap機能の警告、メッセージ駆動Beanの警告、AggregateObjectMappingネストされた外部キーの警告、I/Oエラーなどが表示されます。これらは安全に無視できます。アプリケーションの機能には影響しません。