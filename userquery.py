#!/usr/bin/python
from random import randint
from random import choice

fo=open("DSdatda.sql","r+")
fo.seek(0)
fo.write("-- user service:\n")
user=""
for i in range(1,51):
    user="customer"
    if i > 30:
        user="employee"
    if i > 40:
        user="warehouse_keeper"
    if i >45:
        user="supervisor"
    fo.write("insert into user values('user"+str(i)+"','user"+str(i)+"','user"+str(i)+ "@hua.gr' ,"+str(i)+", '"+user+"' );\n")
for i in range(1,31):
    if i < 10:
        fo.write("insert into customer values("+str(i)+",'workshop"+str(i)+"','000000000"+str(i)+"' ,'wsuper"+str(i)+"', 210000000"+str(i)+" );\n")
    else:
        fo.write("insert into customer values("+str(i)+",'workshop"+str(i)+"','00000000"+str(i)+"' ,'wsuper"+str(i)+"', 21000000"+str(i)+" );\n")
    for j in range(1,6):
        fo.write("insert into company values("+str(i)+",'company"+str(randint(1,20))+"');\n")
for i in range(0,10):
    j=i+31
    k=(i%5)+46
    fo.write("insert into employee values ("+str(j)+","+str(k)+",'employee"+str(i)+"');\n")
for i in range(1,6):
    j=i+40
    fo.write("insert into warehouse_keeper values("+str(j)+",'shop"+str(i)+"','warehouse keeper "+str(i)+"');\n")
for i in range(1,6):
    j=i+45
    fo.write("insert into supervisor values("+str(j)+",'supervisor"+str(i)+"');\n")
fo.write("-- spare parts\n")
foo = ["used","new"]
for i in range(1,501):
    part_id = str(i)
    availability = str(randint(0,30))
    name = "spare part init" + str(i)
    manufacturer = "man" + str(randint(1,20))
    price = str(randint(30,1500))
    part_type = choice(foo)
    fo.write("insert into spare_part values("+part_id+","+availability+",'"+name+"','"+manufacturer+"',"+price+",'"+part_type+"');\n")
    for j in range(1,randint(3,8)):
        fo.write("insert into supported_car values("+part_id+",'carmodel"+str(randint(1,46))+"');\n")
fo.write("-- transactions\n")
fo.write("-- CREATE IF NECESSARY...\n")

fo.truncate()
fo.close()
