알고리즘
현재 저의 텀 프로젝트 분석 프로그램은 자바로 이루어 져있습니다.
먼저 전체적인 구현 방법은 이동평균선을 구하여 매매 지점을 구합니다.
이동 평균선은 주식 매매에 있어 많은 사람들이 참고 하는 자료이고 많은 정보가 있기 때문에 무시 할 수 없는 자료 입니다.

아래 그림은 사고 파는 알고리즘을 코드로 구현한 자료 입니다.
먼저 각각의 TICK을 분석하여 5,10,20,60,120 틱 평균 선을 구합니다
각각 a,b,c,d,e,변수로 할당 되어 있습니다. 이의 증감, 감소 등의 방법으로 여러가지 시도해보았으나 거래가 많으면 수수료가 많고, 거래가 적으면 손실이 많이 일어나게 되어 아래와 같이 구성하게 되었습니다.

주식 구입 시점은 아래와 같습니다
5, 10 , 20 ,60, 120 이평선이 모두 상향을 그리고 있고 같은 순서로 위치하고 있을 때,
그리고 이전 가격과의 Gap이 3퍼센트 이상일 때 상승 돌파 구간이라고 가정하였습니다.
왜냐하면 한번의 틱에 3퍼센트 이상 올라간다는 뜻은 앞으로도 돌파할 수 있는 에너지가 존재한다는 뜻이기 때문입니다.

손절매는 최대 가격의 10퍼센트
최대 이익 매매는 산 가격기준 15퍼센트
처음 가격의 30퍼센트 이하가 되면 팔도록 지정하였습니다.
