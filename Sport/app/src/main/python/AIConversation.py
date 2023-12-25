import SparkApi

# 以下密钥信息从控制台获取
appid = "fb3e9e6f"  # 填写控制台中获取的 APPID 信息
api_secret = "NGY1ODQ5NTlkZDVhODUyODIyMGM3OWY0"  # 填写控制台中获取的 APISecret 信息
api_key = "0ee28cde1ebfefdfc03cf94404e66a74"  # 填写控制台中获取的 APIKey 信息

# 用于配置大模型版本，默认“general/generalv2”
domain = "generalv3"  # 对应3.*版本
# domain = "general"   # v1.5版本
# domain = "generalv2"    # v2.0版本
# 云端环境的服务地址
# Spark_url = "ws://spark-api.xf-yun.com/v1.1/chat"  # v1.5环境的地址
# Spark_url = "ws://spark-api.xf-yun.com/v2.1/chat"  # v2.0环境的地址
Spark_url = "ws://spark-api.xf-yun.com/v3.1/chat"  # v3.1环境的地址


def getlength(text):
    length = 0
    for content in text:
        temp = content["content"]
        leng = len(temp)
        length += leng
    return length


def checklen(text):
    while (getlength(text) > 8000):
        del text[0]
    return text


#  返回回答内容，为空时说明网络错误，则返回
def conversation(role_msg, prompt_msg):
    roles = str(role_msg).split('#')
    prompts = str(prompt_msg).split('#')

    message = []
    for i in range(0, len(roles)):
        # jsoncon = {}
        # jsoncon["role"] = roles[i]
        # jsoncon["content"] = prompts[i]
        # message.append(jsoncon)

        pair = {'role': roles[i], 'content': prompts[i]}
        message.append(pair)

    print(message)

    SparkApi.answer = ""
    SparkApi.main(appid, api_key, api_secret, Spark_url, domain, checklen(message))
    answer = SparkApi.answer
    if answer == "":
        return "Internet Error"
    else:
        return SparkApi.answer


if __name__ == "__main__":
    role = "user#assistant#user#assistant#user#user#user"
    prompt = "aaaaa#bbbbb#ccccc#ddddd#eeeee#你是谁#Can you repeat my first question?"
    # role = "assistant#user"
    # prompt = "Hello, I'm ChatGLM3-6B, can I help you?#please tell me about yourself"
    print(conversation(role, prompt))

