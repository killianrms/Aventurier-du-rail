data = """\
Athina Angora 5
Budapest Sofia 5
Frankfurt Kobenhavn 5
Rostov Erzurum 5
Sofia Smyrna 5
Kyiv Petrograd 6
Zurich Brindisi 6
Zurich Budapest 6
Warszawa Smolensk 6
Zagrab Brindisi 6
Paris Zagreb 7
Brest Marseille 7
London Berlin 7
Edinburgh Paris 7
Amsterdam Pamplona 7
Roma Smyrna 8
Palermo Constantinople 8
Sarajevo Sevastopol 8
Madrid Dieppe 8
Barcelona Bruxelles 8
Paris Wien 8
Barcelona Munchen 8
Brest Venezia 8
Smolensk Rostov 8
Marseille Essen 8
Kyiv Sochi 8
Madrid Zurich 8
Berlin Bucuresti 8
Bruxelles Danzic 9
Berlin Roma 9
Angora Kharkov 10
Riga Bucuresti 10
Essen Kyiv 10
Venizia Constantinople 10
London Wien 10
Athina Wilno 11
Stockholm Wien 11
Berlin Moskva 12
Amsterdam Wilno 12
Frankfurt Smolensk 13
Lisboa Danzic 20
Brest Petrograd 20
Palermo Moskva 20
Kobenhavn Erzurum 21
Edinburgh Athina 21
Cadiz Stockholm 21"""

for line in data.split('\n'):
    line = line.strip()
    ville1, ville2, score = line.split(' ')
    print(f'missions.add(new Mission("{ville1}", "{ville2}", {score}));')
