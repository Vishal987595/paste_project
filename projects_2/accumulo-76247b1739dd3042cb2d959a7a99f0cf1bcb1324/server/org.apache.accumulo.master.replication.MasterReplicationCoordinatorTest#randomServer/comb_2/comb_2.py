import itertools
import subprocess

stuff = []
stuff.append(
    "org.apache.accumulo.master.replication.MasterReplicationCoordinatorTest#randomServerFromMany")
stuff.append(
    "org.apache.accumulo.master.replication.MasterReplicationCoordinatorTest#invalidOffset")

target = "org.apache.accumulo.master.replication.MasterReplicationCoordinatorTest#randomServer"
s = []
for L in range(0, len(stuff)+1):
    for subset in itertools.combinations(stuff, L):
        s.append(list(subset) + [target])


for i, comb in enumerate(s):
    print(i)
    cmand = "mvn -Drat.skip=true test -Dtest=%s" % ','.join(map(str, comb))
    file = open(f"out.{i}.txt", "a")
    file.write(cmand + "\n\n")
    process = subprocess.Popen(
        cmand, stdout=file, stderr=file, shell=True)
    process.communicate()