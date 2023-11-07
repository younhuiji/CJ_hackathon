import pandas as pd
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity

class AlcoholRecommender:
    def __init__(self, data_path):
        # 데이터 불러오기
        self.df = pd.read_csv(data_path, header=0)
        self.df['foodsPoint'] = self.df['foodsPoint'].fillna('')

        # 열에 접두사 추가 및 'flavors', 'combined' 열 생성
        self.df['sweetFlavor'] = 'sweet_' + self.df['sweetFlavor'].astype(str)
        self.df['sourFlavor'] = 'sour_' + self.df['sourFlavor'].astype(str)
        self.df['bodyFlavor'] = 'body_' + self.df['bodyFlavor'].astype(str)
        self.df['carbonicFlavor'] = 'carbon_' + self.df['carbonicFlavor'].astype(str)
        self.df['tanninFlavor'] = 'tannin_' + self.df['tanninFlavor'].astype(str)
        self.df['flavors'] = self.df['sweetFlavor'] + ',' + self.df['sourFlavor'] + ',' + self.df['bodyFlavor'] + ',' + self.df['carbonicFlavor'] + ',' + self.df['tanninFlavor']
        self.df['combined'] = self.df['foodsPoint'] + ',' + self.df['flavors']

    def search_index(self, input_name):
        # 입력된 술 이름에 해당하는 인덱스 찾기
        try:
            target_index = self.df[self.df.iloc[:, 1] == input_name].index[0]
            return target_index
        except IndexError:
            return "술 이름을 찾을 수 없습니다."

    def calculate_cosine_similarity(self):
        # 텍스트 데이터를 벡터화 시키고 코사인 유사도 구하기
        count_vect = CountVectorizer(min_df=1, ngram_range=(1, 2))
        data_mat = count_vect.fit_transform(self.df['combined'])
        similarity_matrix = cosine_similarity(data_mat, data_mat)
        return similarity_matrix

    def find_sim_alcohol(self, product_name, top_n=3):
        target_index = self.search_index(product_name)
        if isinstance(target_index, str):
            return target_index

        similarities = self.calculate_cosine_similarity()
        similar_indices = similarities[target_index].argsort()[::-1][:top_n]

        top_similar_drinks_data = self.df.iloc[similar_indices]
        top_similar_drinks_similarities = similarities[target_index, similar_indices]

        top_similar_drinks_data.loc[:,'유사도'] = top_similar_drinks_similarities
        top_similar_drinks_data = top_similar_drinks_data.drop(index=target_index)
        return top_similar_drinks_data

# 객체 생성
recommender = AlcoholRecommender('C:/Users/user/Desktop/recommend/final.csv')

# 유사한 제품 검색 (입력 제품 하나만 제외)
similar_products = recommender.find_sim_alcohol('양지백주', top_n=3).values
# similar_products = recommender.find_sim_alcohol('양지백주', top_n=3).values.tolist()
print(similar_products)
