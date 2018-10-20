# 일상 속에 하루를 담다, 다정봇

다정봇은 당신의 하루를 챗봇과의 대화 중 자연스럽게 기록해줄 챗봇 다이어리입니다. 다이어리는 소중한 사람과 함께 했던 즐겁고 행복했던 기억들을 오랫동안 기억할 수 있게 해줍니다. 기존에 다이어리를 쓰다보면 하루 이틀 밀려 다이어리 쓰는 것을 관두게 되고, 하루 중 해야할 일이 된 것만 같아 느끼게 되는 부담감에 다이어리 쓰는 것을 꺼리게 되는 불편함을 개선하고자 하였습니다. 챗봇과의 친구와 대화를 하며 자신의 하루를 공유하는 것과 같이 챗봇과의 대화를 통해 공유된 정보들을 저장하고 다시 한 번 꺼내볼 수 있게 하는 되었습니다.


## Functions

> **일정 등록**

- 사용자가 자신의 일정을 챗봇에게 이야기할 수 있습니다. 챗봇은 사용자의 일정에 대해 언제, 어디서, 무엇을 하는지에 대해 질문을 하게 됩니다. 
- 사용자는 "나 할 일 생겼어!", "내일 홍대에서 멘토링 할거야" 와 같은 메세지를 통해 챗봇에게 자신의 일정에 대해 알릴 수 있습니다. 

> **일정 알림**

- 사용자가 등록한 일정에 대해 잊어버리지 않도록 푸시알림과 함께 메세지를 보내줍니다.
- 사용자가 일정 알림을 받을 시간에 대해 설정할 수 있도록 하였으며, 매일 지정한 시간마다 메세지를 통해 일정에 대해 안내합니다.

> **일정 질문**

- 사용자가 등록한 일정이 끝날 시간에 사용자에게 일정이 어땠는지 질문을 하여 사용자로부터 그 일정에 대한 이야기를 듣습니다.
- 사용자의 다이어리는 이 질문에 대한 답변을 기반으로 하여 기록하게 됩니다.

> **추억 회상**

- 사용자가 등록했던 지난 일정들에 대해 사용자는 물어볼 수 있습니다. 
- 챗봇은 일정 등록 기능과 일정 질문 기능에서 사용자가 대답한 대화를 기반으로 하여 데이터를 보내줍니다.
- "어제 뭐했더라?", "2017년 4월 1일에 뭐했지?", "작년 크리스마스에는 어떤 일이 있었어?"와 같은 메세지를 통해 자신의 추억을 돌아 볼 수 있습니다.

> **캘린더**

- 사용자는 챗봇과의 대화를 통해 저장된 자신의 기록들을 캘린더를 통해 한눈에 확인할 수 있습니다.
- 캘린더를 통해 아래와 같은 문제점을 개선할 수 있었습니다.
  * 챗봇과의 대화만으로는 자신이 어떤 어떤 일들을 했는지 한눈에 확인하기 힘든 점
  * 챗봇과의 대화를 통해 이루어지는 번거러운 수정과 삭제 과정을 단순화
  
> **자동완성**

- 사용자는 자동 완성 기능을 통하여 빈칸을 채워나가며 챗봇과 대화 할 수 있습니다.
- 자동완성을 통해 아래와 같은 문제점을 개선할 수 있었습니다. 
  * 사용자가 어떠한 말을 통해 챗봇에게 말을 걸어야 할지 모르는 막막함
  * 메세지를 보냈지만 채트플로우에 맞지 않게 진행되는 경우
  * 긴 문장들을 작성하기 귀찮음


> **캐릭터별 말투**

- 다정봇은 4가지의 캐릭터를 만들어 사용자가 다양한 캐릭터와 함께 대화 할 수 있도록 하였습니다.
- 캐릭터별 말투를 다르게 하였습니다.
- 캐릭터에 대한 자세한 소개는 아래 [Chracters](#characters)를 참고해주세요.


## Characters


<img src="https://bit.ly/2OI65m8" alt="일정등록" width="15%"/><img src="https://bit.ly/2zXBhWg" alt="일정등록" width="15%"/><img src="https://bit.ly/2zWBEjP" alt="일정등록" width="15%"/><img src="https://bit.ly/2QBdUan" alt="일정등록" width="15%"/>


* 다정군 : 토끼 캐릭터로 스윗한 말을 건내주곤 합니다. 하지만 다소 _오글거리는(?)_ 표현들이 존재합니다.
* 다정냥 : 고양이 캐릭터로 발랄한 친구입니다. 때로는 귀여운 동생같은 친구랍니다.
* 다정곰 : 곰 캐릭터로 차분하고 포근한 친구입니다. 하지만 누군가는 그를 답답하게 느낄 수도 있답니다.
* 다정뭉 : 강아지 캐릭터로 천방지축한 친구입니다. 한 눈 파는 사이에 사고를 칠 것만 같은 친구지요!

## Video

[![Video Label](http://img.youtube.com/vi/IB5ciGW4e84/0.jpg)](https://youtu.be/IB5ciGW4e84?t=0s) 

## Screenshots
<img src="https://bit.ly/2OHTM9f" alt="스플래시" width="20%"/><img src="https://bit.ly/2y4ziOE" alt="로그인" width="20%"/><img src="https://bit.ly/2y4zsWg" alt="회원가입" width="20%"/><img src="https://bit.ly/2y4zxcw" alt="채팅" width="20%"/><img src="https://bit.ly/2OHxPap" alt="캘린더" width="20%"/>

_스플래시 / 로그인 / 회원가입 / 채팅 / 캘린더_


### Detail Screenshots
채팅에 대한 자세한 스크린샷 입니다.
> 일정 등록 / 자동완성

<img src="https://bit.ly/2OJ6kgR" alt="일정등록" width="40%"/><img src="https://bit.ly/2NvY674" alt="자동완성" width="40%"/>

> 일정 알림

<img src="https://bit.ly/2Nu77gS" alt="일정알림" width="40%"/><img src="https://bit.ly/2E2Yryr" alt="FCM알림" width="40%"/>

> 일정 질문 / 추억 회상

<img src="https://bit.ly/2NsQzpx" alt="일정질문" width="40%"/><img src="https://bit.ly/2C1z4KW" alt="일정질문" width="40%"/>


## Downloads

[<img src="https://play.google.com/intl/ko/badges/images/generic/ko_badge_web_generic.png" alt="플레이스토어" width="300px">](https://play.google.com/store/apps/details?id=com.dajeong.chatbot.dajeongbot)

현재 누적 다운로드 수 **307 건**
(2018.09.14. ~2018.10.14. 기준)


## Extras
- **제작 중에 있어 프로그램이 다소 불안정한 부분이 존재합니다.**
- 서버에 대한 정보는 [Dajeongbot-server](https://github.com/zoripong/DaJeongBot-Server) 를 참고해주세요.
- author [zoripong](https://github.com/zoripong/)
- CONTACT ME : <mailto:devuri404@gmail.com>
