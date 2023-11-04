import random

num_of_numbers = int(input())

for _ in range(num_of_numbers):
    random_number = random.randint(1, 100)  # 1 ile 100 arasında rastgele bir sayı üretin
    print(random_number)
