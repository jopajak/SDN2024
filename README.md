# SDN2024: QoS for a specific type of data traffic

Projekt __"QoS for a specific type of data traffic"__ realizowany w ramach przedmiotu _Sieci Sterowane Programowo_.

Realizowany przez:
* Jakub Dobosz
* Joanna Pająk
* Magdalena Podsiadło

## Opis projektu 

Celem projektu jest zademonstrowanie działania priorytetyzacji przepływów opartej na numerach portów warstwy czwartej TCP/IP. 
Sieć składa się z czterech przełączników (dokładna topologia poniżej) i dwóch urządzeń końcowych. W sieci zdefiniowaliśmy dwa rodzaje przepływów - ruch priorytetowy voice i ruch tła. W sytuacji, gdy w sieci nie ma natłoku, pakiety głosowe są wysyłane współdzielonym łączem. Gdy szybkość transmisji wzrośnie powyżej 70% maksymalnej przepustowości, nowe przepływy głosowe są kierowane na dedykowane łącze w celu utrzymania pożądanych parametrów QoS.

## Kod kontrolera Floodlight

Repozytorium zawiera klasy kontrolera Floodlight v1.2, które zostały zmienione w ramach naszego projektu.

### Pełny kod źródłowy
Pełny kod źródłowy kontrolera Floodlight v1.2, który można bezpośrednio rozpakować i uruchomić, znajduje się pod adresem:  
[Pełny kod źródłowy Floodlight v1.2](https://drive.google.com/drive/folders/1qnSGO91XvutRrXoLWzKFYeglNx3ZJCt6?usp=sharing)  
*(Nie mógł być umieszczony na GitHubie, ponieważ przekracza maksymalny dozwolony rozmiar pliku).*

### Zmodyfikowane pliki
Dokonywaliśmy zmian jedynie w plikach, które znajdują się w folderze: \src\main\java\pl\edu\agh\kt w kodzie z laboratorium 7. W związku z tym, w repozytorium zamieściliśmy wszystkie klasy z tego miejsca w folderze **`Floodlight-SDN-Code`**.

## Topologia sieci

![topologia_my_topo](https://github.com/user-attachments/assets/327822bb-de18-4d74-846b-41ffae9960f8)


## Diagram pseudokodu:

![obraz](https://github.com/user-attachments/assets/fb100162-14c6-4c15-a4eb-85d2a875b2d1)


## Wymagania
* Środowisko wirtualizacyjne (e.g. Oracle VirtualBox) z zainstalowaną maszyną wirtualną Floodlight\
* Mininet\
* hping3 (*sudo apt-get install hping3*)\
* Netcat (*sudo apt install netcat*)\
* Xterm (optional)

## Uruchomienie
1. Uruchomić maszynę wirtualną.
2. Pobrać kod projektu, pliki h1.sh, h2.sh służące do generacji ruchu oraz Topo.py (zawierający topologię)
3. Uruchomić kontroler floodlight z użyciem IDE (e.g. Eclipse) lub z terminala (w katalogu głównym kontrolera wydać następujące polecenie: *java -jar target/floodlight.jar*)
4. Otworzyć inną konsolę, sprawdzić IP kontrolera poleceniem *ifconfig* (lub użyć adresu 127.0.0.1)
5. Uruchomić środowisko Mininet poleceniem: *sudo mn --custom /path/to/file/Topo.py --topo mytopo --link=tc --controller=remote,ip=127.0.0.1,port=6653*
6. W konsoli mininet wydać polecenie: xterm h1 h2 s1
7. W konsoli hosta h2 uruchomić skrypt nasłuchujący: *bash /path/to/file/h2.sh*\
8. W konsoli hosta h1 uruchomić skrypt generujący ruch: *bash /path/to/file/h1.sh*\
9. W konsoli przełącznika s1 można wydać polecenie: *ovs-ofctl dump-flows s1* (odpowiednio s1, s2, s3, s4 w celu zaobserwowania dodawania poszczególnych wpisów przepływów)


