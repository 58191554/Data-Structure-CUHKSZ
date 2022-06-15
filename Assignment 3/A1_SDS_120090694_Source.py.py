import random


def print_introduction():
    print("*SLIDING PUZZLE* \n Users can choose any dimensions up to 10x10, minimum\n",
          "dimension is 3x3.The board has an empty space where an adjacent tile can be\n",
          "slid to.The following figure shows an example of an 4x4 puzzle")
    prinPuzzle(ans(4), 4)


def input_d():  # define a robust function to input the size of puzzle as d
    while True:
        try:
            d = int(input("Enter the desired dimension of the puzzle d = "))
            if d in range(3, 11):  # restrict the range from 3 to 10
                break
            else:
                print("Out of range[3,10], Try again...")

        except:
            print("invalid number, Try again...")
    return d


def ans(d):  # Define a two-dimensional list of d*d size answer.
    l_ans = []  # save the answer in l_ans
    l_list_i = []  # create the sub-list in the l_ans
    # take the j (th) number in group i, and put them into list_i.
    for i in range(0, d):
        for j in range(d*(i) + 1, d*(i+1) + 1):
            l_list_i.append(j)

        # put list_i into a bigger list, the answer list.
        l_ans.append(l_list_i)
        l_list_i = []
    l_ans[-1][-1] = 0  # change the last number into 0
    return l_ans


def prinPuzzle(l_puz, d):  # The function is to change the 2D list into real puzzle
    print("="*3*d)
    for i in range(len(l_puz)):

        for j in l_puz[i]:  # Iterate each number in the sub-list

            if j == 0:  # Change the 0 block into " "(blank space)
                print("   ", end="")
            else:
                print("%3d" % j, end="")

            if l_puz[i].index(j) % d == d-1:
                print(end="\n")

    print("="*3*d)


def gamepad():
    # Use dictionary to store button
    l_pad_dict = {"up": "up", "down": "down", "left": "left", "right": "right"}

    for i in range(len(l_pad_dict)):
        print("define {} as :".format(list(l_pad_dict.keys())[i]), end=" ")
        l_v_i = input()

        # Check out whether the button has already been used or too many letter.
        while l_v_i in list(l_pad_dict.values()) or len(l_v_i) != 1 or l_v_i.isalpha() == False:

            if l_v_i.isalpha() == False:
                print("You need to enter a letter!!!\nDefine {} as :".format(
                    list(l_pad_dict.keys())[i]), end=" ")

            if l_v_i in list(l_pad_dict.values()):
                print("The button has already been used.Try again!!!\nDefine {} as :".format(
                    list(l_pad_dict.keys())[i]), end=" ")

            if len(l_v_i) != 1:
                print("The button should be a single letter.Try again!!!\nDefine {} as :".format(
                    list(l_pad_dict.keys())[i]), end=" ")
            l_v_i = input()
        l_pad_dict[list(l_pad_dict.keys())[i]] = l_v_i
        print("You successfully difine ", list(l_pad_dict.keys())
              [i], "as", list(l_pad_dict.values())[i])

    print("Your DIY keyboard is", l_pad_dict)
    return l_pad_dict


def find_zero(l_2d_list):
    # Find the zero in the list by interate the main list and sublist

    for i in range(len(l_2d_list)):  # iterate each number in the list
        for j in l_2d_list[i]:
            if j == 0:
                zero_index = (i, l_2d_list[i].index(j))
                break
            else:
                continue
    return zero_index


def dictionary_of_hint(l_zero_index, puz):
    global d, pad_dict  # From the timely zero situation, we make hint of -
    dict_hint = {}  # availible direction. I use dictionary to store message
    l_zero_index = find_zero(puz)

    if l_zero_index[0] != d-1:  # l_zero_index[0] is the sublist index zero in.
        dict_hint['UP'] = pad_dict["up"]
    if l_zero_index[0] != 0:
        dict_hint['DOWN'] = pad_dict["down"]
    if l_zero_index[-1] != d-1:  # l_zero_index[-1]: zero is in the l_zero_index[-1]position
        dict_hint['LEFT'] = pad_dict["left"]  # of sublist
    if l_zero_index[-1] != 0:
        dict_hint['RIGHT'] = pad_dict["right"]

    return dict_hint


def formPuzzle():  # Create the random puzzle in the base of answer list
    global d, pad_dict
    zero_index = find_zero
    l_arr = ans(d)
    hint = dictionary_of_hint(zero_index, l_arr)
    n = 0  # loop:disrupt answer,like disrupt the Rubik's cube
    while n < 10000:
        zero_index = find_zero(l_arr)
        # judge aviliable dicrection
        hint = dictionary_of_hint(zero_index, l_arr)

        # Select a direction randomly in the hint
        random_dir = random.choice(list(hint.values()))

        # Use motion function to execute the random dirction
        l_arr = Move(random_dir, l_arr)
        n += 1
    return l_arr


def operation():  # Use this function to prompt user to decide decision
    zero_index = find_zero(puzzle)
    hint = dictionary_of_hint(zero_index, puzzle)

    print("Enter your move (", end="")
    for i in hint.keys():  # those directions in the hint is availible
        print(i, "-", hint[i], end=", ")
    print(")")
    mnplt = input()  # Prompt the user to input a direction as mnplt

    while True:  # judge whether it is availible
        if mnplt in hint.values():
            break
        else:
            prinPuzzle(puzzle, d)
            print("What you press is out of the button range\
                \nMove the adjacent tile to the blank space, you can choose (", end="")
            for i in hint.keys():
                print(i, "-", hint[i], ", ", end="")
            print(")")
            mnplt = input()
    return mnplt


# take in direction and a specfic puzzle,generate a new list.
def Move(direc, puz):
    global pad_dict
    zero_index = find_zero(puz)
    # use x, y to represent the position of 0
    x, y = zero_index[0], zero_index[1]

    # Make position change parameter a,b for row and column.
    if direc == pad_dict['up']:
        # They control add and minuslike two pair of switch
        a, b = 1, 0
    if direc == pad_dict['down']:
        a, b = -1, 0
    if direc == pad_dict['right']:
        a, b = 0, -1
    if direc == pad_dict['left']:
        a, b = 0, 1

    temp_value = puz[x+a][y+b]  # change the value of the position
    puz[x][y] = temp_value
    puz[x+a][y+b] = 0
    return(puz)


print_introduction()  # 1.print instruction3.form gamepad.

while True:
    d = input_d()  # 2.input d
    pad_dict = gamepad()
    puzzle = formPuzzle()  # 3.form gamepad.
    print("#WAIT FOR YOUR PUZZLE")
    prinPuzzle(puzzle, d)
    count = 0

    while puzzle != ans(d):  # The loop is to check when user solve the puzzle
        oper = operation()
        puzzle = Move(oper, puzzle)
        prinPuzzle(puzzle, d)
        count += 1
    print("Congratulations! You solved the puzzle in {} moves!".format(count))

    while True:  # Restart action
        try:
            option = input("enter 'q' to play it again or enter 'n' exit:")
            if option in ['q', 'n']:
                break
        except:
            print("this is not an available choice. Try again!!!")

    if option == "q":
        print('over')
        break
    if option == "n":
        continue
