# Bodylog

The main goal of this (WIP) project is to demonstrate an implementation of the Model-View-Intent (MVI) design pattern, which aims at having a unidirectional data flow where application state is driven by the business logic.

The project follows the "clean" approach for architecting apps and is composed of several modules (both Android and pure java), namely:
- framework (UI)
- data (Repository)
- adapter (UI presentation)
- domain (Business logic)
 
_Presenter_(s) in this implementaion have **no** lifecycle. They implement the **_Disposable_** interface and expose couple of additional methods, including **_bindIntents()_**, **_unbindIntents()_** and optionally **_attachView(View)_** and **_detachView()_**. 

Internally, Presenters create **_PublishRelay_**, which subscribes to the stream of _View_ intents and serve as a gateway to the business logic of the app. Communication between _Presenter_ and business logic is established by using *_Interactor_*s which can also be shared between other *_Presenter_*s. When there is a screen orientation change, _View_ detaches from _Presenter_ by unsubscribing the **_PublishRelay_** from the _View_. When the _View_ attaches back to the _Presenter_, the **_PublishRelay_** subscribes back to the _View_'s stream of intents.

In addition, presenters create **_BehaviourRelay_**_(s)_ that work as a gateway from business logic to _View_. While a _View_ is detached from a _Presenter_, say we navigate forward from Activity A to Activity B, the presenter of Activity A will be still alive and can receive updates from the business logic. When we navigate back and _View_ reataches to _Presenter_, the _BehaviourRelay_ will replay the cached model update and _View_ will update accordingly.

In order to preserve _Presenter(s)_ in memory during orientation change, they are injected into a **_PresenterHolder_** that extends from Android's _ViewModel_ (https://developer.android.com/topic/libraries/architecture/viewmodel.html). Throughout the lifecycle of an activity/fragment, _View_(s) get detached and reatached to _Presenter_(s), so no memory leaks would occur. When an _Activity_ or _Fragment_ gets finally destroyed (not due to orientation change), _Presenter_ is also destroyed.

## Libraries used in this project

### UI libraries 
- AppCompat
- Butterknife

### Reactive libraries
- RxJava2
- RxAndroid
- RxRelay2
- Lifecycle _(for Android's ViewModel)_

### Others
- Dagger _(dependency injection)_
- AutoValue _(immutability)_

### Debugging & Inspecting
- LeakCanary _(finding memory leaks)_

### Testing
- JUnit
- Mockito
