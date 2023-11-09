from selenium import webdriver
from selenium.webdriver import ActionChains # 일련의 작업들을(ex.아이디 입력, 비밀번호 입력, 로그인 버튼 클릭...) 연속적으로 실행할 수 있게 하기 위해
from selenium.webdriver.common.keys import Keys # 키보드 입력을 할 수 있게 하기 위해
from selenium.webdriver.common.by import By # html요소 탐색을 할 수 있게 하기 위해
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support.ui import WebDriverWait # 브라우저의 응답을 기다릴 수 있게 하기 위해
from selenium.webdriver.support import expected_conditions as EC # html요소의 상태를 체크할 수 있게 하기 위해
from selenium.common.exceptions import NoSuchElementException
import urllib.request
import time

from collections import OrderedDict

#categories = ['wine', 'soju']
categories = ['takju']
url = 'https://www.sooldamhwa.com/damhwaMarket/listing/{}'

driver = webdriver.Chrome()
datas = {}

def txt2num(txt):
    if txt == 'zero':
        return 0
    elif txt == 'one':
        return 1
    elif txt == 'two':
        return 2
    elif txt == 'three':
        return 3
    elif txt == 'four':
        return 4
    elif txt == 'five':
        return 5
    else:
        return -1

for category in categories:
    img_idx = 0
    
    # 페이지 로드
    driver.get(url.format(category))

    # 스크롤을 끝까지 내리기
    while True:
        scroll = driver.execute_script('return document.body.scrollHeight')
        driver.execute_script('window.scrollTo(0, document.body.scrollHeight)')
        time.sleep(0.3)
        height = driver.execute_script('return document.body.scrollHeight')

        if scroll == height:
            break

        scroll = driver.execute_script('return document.body.scrollHeight')

    # 테스트용
    #limit = 2

    # 각 술 링크 가져오기
    names = driver.find_elements(By.CLASS_NAME, 'card-content-title-wrapper')
    for name in names:
        data = {}
        data['category'] = category
        data['url'] = name.find_element(By.XPATH, '../..').get_attribute('href')
        datas[name.text] = data

        # 테스트용
        #limit -= 1
        #if limit == 0:
        #    break

    print(len(names))

    wait = WebDriverWait(driver, 10)

    # 상세 정보 가져오기
    for key in datas.keys():
        driver.get(datas[key]['url'])

        wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, '#image img')))

        img_url = driver.find_element(By.CSS_SELECTOR, '#image img').get_attribute('src').replace('https', 'https')
        img_path = "images/{}_{}.jpg".format(category, img_idx)
        img_idx += 1
        urllib.request.urlretrieve(img_url, img_path)

        datas[key]['imagePath'] = img_path
        datas[key]['comment'] = driver.find_element(By.CLASS_NAME, 'DetailProductCardStyle__SubTitle-sc-khnr6w-5').text
        
        datas[key]['score'] = driver.find_element(By.CLASS_NAME, 'score').text
        datas[key]['reviewCount'] = driver.find_element(By.CLASS_NAME, 'review').text
        if datas[key]['reviewCount'] == '':
            datas[key]['reviewCount'] = '0'
        else:
            datas[key]['reviewCount'] = datas[key]['reviewCount'].split()[0].replace('[', '')

        details = driver.find_elements(By.CSS_SELECTOR, '.sector.third > div')
        datas[key]['kind'] = details[0].find_element(By.TAG_NAME, 'span').text
        datas[key]['alcoholDegree'] = details[1].find_element(By.TAG_NAME, 'span').text
        datas[key]['capacity'] = details[2].find_element(By.TAG_NAME, 'span').text

        try:
            details = driver.find_elements(By.CSS_SELECTOR, '.SoolMainPointsStyle__SoolMainPointsWrapper-sc-hq7xx3-0 > div')
            datas[key]['tastePoint'] = details[1].find_elements(By.TAG_NAME, 'span')[1].text
            datas[key]['foodsPoint'] = details[2].find_elements(By.TAG_NAME, 'span')[1].text
            datas[key]['specialPoint'] = details[3].find_elements(By.TAG_NAME, 'span')[1].text
        except IndexError:
            datas[key]['tastePoint'] = ''
            datas[key]['foodsPoint'] = ''
            datas[key]['specialPoint'] = ''

        # 어울리는 음식 가져오기 (없을 경우 공백)
        try:
            datas[key]['foods'] = []
            details = driver.find_elements(By.CLASS_NAME, 'FittingFoodsStyle__Content-sc-biizcl-1 > div')
            for detail in details:
                datas[key]['foods'].append(detail.find_element(By.TAG_NAME, 'label').text)
        except NoSuchElementException:
            pass

        flavorIdx = 0
        details = driver.find_elements(By.CLASS_NAME, 'taste-rating-bar')

        datas[key]['sweetFlavor'] = -1
        datas[key]['sourFlavor'] = -1
        datas[key]['bodyFlavor'] = -1
        datas[key]['carbonicFlavor'] = -1
        datas[key]['tanninFlavor'] = -1

        for detail in details:
            tag = detail.find_element(By.CLASS_NAME, 'SubscriptionHintRatingStyles__RatingName-sc-8yfc2n-1').text
            rating = txt2num(detail.find_element(By.CSS_SELECTOR, 'img:nth-child(2)').get_attribute('src').split('_')[-1].split('.')[0])
            
            flavor = ''
            if tag == '단맛':
                flavor = 'sweetFlavor'
            elif tag == '산미':
                flavor = 'sourFlavor'
            elif tag == '바디':
                flavor = 'bodyFlavor'
            elif tag == '탄산':
                flavor = 'carbonicFlavor'
            elif tag == '탄닌' or tag == '씁쓸':
                flavor = 'tanninFlavor'
            else:
                continue
            
            datas[key][flavor] = rating

f = open('output.txt', 'w')
for key in datas.keys():
    cols = list(datas[key].keys())
    cols.insert(1, 'name')
    f.write(','.join(cols))
    f.write('\n')
    break

for alcohol in datas.keys():
    data = datas[alcohol]
    cols = ['"' + data['category'] + '"', '"' + alcohol + '"']

    for key, val in data.items():
        if key == 'category':
            continue

        if key == 'foods':
            cols.append('"' + ','.join(val) + '"')
        elif key[-6:] == 'Flavor':
            cols.append(str(val))
        elif key == 'score' or key == 'reviewCount':
            cols.append(val)
        else:
            cols.append('"' + str(val) + '"')

    row = ','.join(cols)
    f.write(row)
    f.write('\n')

f.flush()
f.close()