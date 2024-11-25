CREATE TABLE author (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL
);

-- Yazılar (Texts)
CREATE TABLE text (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      content TEXT NOT NULL,
                      author_id BIGINT,
                      FOREIGN KEY (author_id) REFERENCES author(id)
);
-- Yazarlar (Authors)
INSERT INTO author (name) VALUES ('Orhan Pamuk');
INSERT INTO author (name) VALUES ('Yaşar Kemal');
INSERT INTO author (name) VALUES ('Elif Şafak');
INSERT INTO author (name) VALUES ('Zülfü Livaneli');
INSERT INTO author (name) VALUES ('Mustafa Kutlu');

-- Yazılar (Texts)

-- Orhan Pamuk'un Yazıları (2 yazı)
INSERT INTO text (content, author_id) VALUES
                                          ('İstanbul, çok yönlü ve çok katmanlı bir şehirdir. Her köşesinde bir tarih yatar, her sokakta bir hikaye vardır. Bu şehri anlamak için bir ömür yetmez. İstanbul bir yazarın gözünde bir masaldan daha fazlasıdır; yaşamın tüm acıları, neşeleri, umutları burada birleşir. Şehri anlatırken, her anın güzelliklerini görmek mümkündür.', 1),
                                          ('Edebiyat bir toplumun aynasıdır. Her toplum kendi gerçeklerini yazara ve edebiyatçılara aktarır. Bizim görevimiz, bu gerçekleri insanlara olduğu gibi, olabildiğince doğru bir şekilde aktarmaktır. Çünkü ancak doğru yazılarla toplumların sorunlarına ışık tutulabilir. Yazının gücü, toplumu dönüştürebilecek kadar güçlüdür.', 1);

-- Yaşar Kemal'in Yazıları (2 yazı)
INSERT INTO text (content, author_id) VALUES
    ('Efsanevi bir toprak parçası, Çukurova… Burada, hayata dair her şeydir; zorluklar, aşk, ayrılık, göç, ama her zaman direnç ve umut vardır. Çukurovanın toprakları, insanları gibi köklüdür. Yalnızca toprağa değil, insanın ruhuna da hayat verir. Bu toprakların gerçek hikayesi, her bireyin içsel direncini keşfetmesinde yatmaktadır.', 2),
('Bir köylünün gözlerinde, tüm bir milletin acısı, sevinci, umudu vardır. İnsanın en zor anlarında dahi doğaya bağlılığını kaybetmemesi, her zaman bir umut ışığı araması beni en derinden etkiler. İnsan ve doğa arasındaki o güçlü bağ her zaman hikayelerime ilham vermiştir. Bir köylü, doğa ile barış içinde yaşamayı öğrenmelidir.', 2);

-- Elif Şafak'ın Yazısı (1 yazı)
INSERT INTO text (content, author_id) VALUES
('Bir kadının sesi, onun içindeki tüm duyguları, korkuları, umutları ve hayallerini yansıtır. Kadınlar, toplumu şekillendiren, dünyayı daha anlamlı kılan bireylerdir. Bir kadının öyküsünü yazmak, sadece onun sesini duymak değil, tüm insanlığın sesine kulak vermek demektir. Kadınların kalemi, dünyayı değiştirmenin en güçlü yoludur.', 3);

-- Zülfü Livaneli'nin Yazıları (4 yazı)
INSERT INTO text (content, author_id) VALUES
                                          ('Müzik, ruhun derinliklerine iner. Bir şarkının sözleri, bir insanın kalbinde kalıcı izler bırakır. Bazen bir melodi, bizi geçmişe götürür, bazen de geleceğe dair umutlar bırakır. Müzik, evrensel bir dil gibi, her dilden, her kültürden insana hitap eder. Her nota, insanın içindeki duyguları dışa vurur.', 4),
                                          ('Sanat, insana insanlığını hatırlatan bir yapıdır. İnsan, toplumun içindeki yerini ancak sanatla anlayabilir. Bir resim, bir şarkı, bir edebi eser, insanın dünyasını yansıtır. Sanat olmadan insanın kimliği kaybolur. Biz de bunu en iyi şekilde ifade etmeye çalışıyoruz. Sanat, insanın yaşamına anlam katar.', 4),
                                          ('Yazı, insanın en derin düşüncelerini, en saf duygularını dışa vurduğu bir mecra olmuştur. Bir yazarın kalemiyle kalbi arasındaki ilişki çok özeldir. Çünkü yazmak, bir insanın içindeki tüm karanlıkları aydınlatmak, aynı zamanda ruhunu özgürleştirmektir. Her yazı, bir insanın içsel dünyasını keşfetmek için bir fırsattır.', 4),
                                          ('Toplumlar geçmişteki hatalarından ders almalı ve geleceğe umutla bakmalıdır. Geçmişin izleri, yarının inşasında önemli bir yer tutar. Her birey, kendi hikayesini yazarken, toplumu da bu hikayeye dahil eder. Toplumlar değişir, ama insanın özündeki değerler değişmez. Her yeni nesil, eski nesillerin mirasını taşır.', 4);

-- Mustafa Kutlu'nun Yazıları (3 yazı)
INSERT INTO text (content, author_id) VALUES
                                          ('Bir köyün sesi vardır. O köyde yaşam, toprağın kokusu, evlerin çatılarına düşen yağmur damlalarının sesi birer melodidir. Her bir köy insanı kendi dünyasını kurar, ama bu dünya bir arada var olur. Geçmişin izleri, geleceğin umutlarıyla harmanlanır. Bu yaşamda en değerli şey, insanın ruhuna dokunan o sessiz anılardır.', 5),
                                          ('Hikayelerimdeki insan, çoğu zaman bir kayıp arayışında olur. Kimi zaman aşk, kimi zaman yalnızlık ve kimi zaman da bir dağ başında kaybolmuş bir umut vardır. Her yazıda, bir insanın iç yolculuğuna tanıklık ederim. Bazen bir kırık dökük ev, bazen de bir dağ, insanın gerçek yüzünü gösterir. Ve her zaman bu yolculuklarda bir ışık vardır.', 5),
                                          ('Bir insanın içindeki derinlikleri yazıya dökebilmek, oldukça zor bir iştir. Ama her kelime, her cümle bir kapı açar. Bu kapılardan içeri girdiğinizde, insanın ruhunu daha yakından tanırsınız. Hikayenin sonunda her zaman bir ışık vardır, o ışık bir umudu simgeler. İnsan ne kadar karanlıkta olsa da, ışığı bulmak her zaman mümkündür.', 5);
