from mysql.connector import (connection)

# 번역 사전 구축
f = open('crawling/updater/translate_new_new_new.txt', 'r')
dict = {}

while True:
    line = f.readline()
    if not line:
        break

    cols = line.split('=')
    keywords = cols[0].split(',')
    translate = cols[1]

    for keyword in keywords:
        realkey = keyword.replace(' ', '')
        if realkey not in dict:
            dict[realkey] = translate.replace('\n', '').replace('\r', '')  

f.close()

f = open('crawling/updater/passed.txt', 'w')

# MySQL 업데이트
conn = connection.MySQLConnection(user='root', password='root',
                                  host='127.0.0.1', database='cjai')

stmt = conn.cursor()
if conn and conn.is_connected():
    with conn.cursor(buffered=True) as cursor:
        cursor.execute('select category, name, foods from alcohol')

        for (category, name, foods) in cursor:
            cols = []
            for food in foods.split(','):
                key = food.replace(' ', '')

                # dict에 없으면 passed.txt에 로그 남기고 넘기기
                if key not in dict:
                    f.write('{}|{}|{}\n'.format(category, name, key))
                    continue

                cols.append(dict[key])
            
            stmt.execute("update alcohol set en_foods = '{}' where category = '{}' and name = '{}'".format(','.join(cols), category, name))
        conn.commit()

f.close()

cursor.close()
conn.close()