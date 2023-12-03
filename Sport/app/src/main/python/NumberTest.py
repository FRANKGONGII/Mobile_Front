import sys
import math

def square(num):
    res = pow(int(num), 2)
    return res

if __name__ == "__main__":
    num = int(sys.argv[1])
    res = square(num)
    print(res)