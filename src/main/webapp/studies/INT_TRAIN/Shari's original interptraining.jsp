<%@ page language="java" import="java.lang.*,java.util.*, org.uva.*" %>
<%
/** stop browser from caching */
response.setHeader("Cache-Control","no-cache,post-check=0,pre-check=0,no-cache");
response.setHeader("Pragma","no-cache");
response.setHeader("Expires","Thu,01Dec199416:00:00GMT");
%>
<%

String[][] stimuli1 = 
{
{
"You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce themselves. After your brief presentation, you guess the others thought you sounded <span style='white-space:nowrap'>con[f]ident.</span>",
"You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce themselves. After your brief presentation, you guess the others thought you sounded <span style='white-space:nowrap'>sh[y].</span>",
"Did you feel dissatisfied with your speech?"
},
{
"A friend suggests that you join an evening class on creative writing. The thought of other people looking at your writing makes you feel <span style='white-space:nowrap'>enthu[s]iastic.</span>",
"A friend suggests that you join an evening class on creative writing. The thought of other people looking at your writing makes you feel <span style='white-space:nowrap'>embarr[a]ssed.</span>",
"Would you expect to feel uncomfortable if others look at your work?"
},
{
"You join a tennis club and before long, you are asked to play in a doubles match. Afterwards you discuss your performance with your partner. Your partner thinks that you played <span style='white-space:nowrap'>br[i]lliantly.</span>",
"You join a tennis club and before long, you are asked to play in a doubles match. Afterwards you discuss your performance with your partner. Your partner thinks that you played <span style='white-space:nowrap'>terri[b]ly.</span>",
"Did your partner feel pleased with your performance?"
},
{
"Your orchestra asks you to play a solo at the next concert. You practice a few times until you feel ready to play it with the orchestra. At the first rehearsal you make one mistake. The conductor will think that your work is <span style='white-space:nowrap'>pro[m]ising.</span>",
"Your orchestra asks you to play a solo at the next concert. You practice a few times until you feel ready to play it with the orchestra. At the first rehearsal you make one mistake. The conductor will think that your work is <span style='white-space:nowrap'>r[u]shed.</span>",
"Did you feel disappointed with your performance?"
},
{
"Your partner asks you to go to an anniversary dinner that his/her company is holding. You have not met any of his/her work colleagues before. Getting ready to go, you think that the new people you will meet will find you <span style='white-space:nowrap'>fri[e]ndly.</span>",
"Your partner asks you to go to an anniversary dinner that his/her company is holding. You have not met any of his/her work colleagues before. Getting ready to go, you think that the new people you will meet will find you <span style='white-space:nowrap'>bo[r]ing.</span>",
"Were you disliked by your new acquaintances?"
},
{
"You receive an essay back from your teacher and did not get the grade that you were expecting. She tells you that this is because on this occasion, your work was <span style='white-space:nowrap'>outst[a]nding.</span>",
"You receive an essay back from your teacher and did not get the grade that you were expecting. She tells you that this is because on this occasion, your work was <span style='white-space:nowrap'>confusi[n]g.</span>",
"Did you get a better grade than you expected?"
},
{
"You have just moved to a new area and your neighbor asks if you would like to go to your local bar that evening. When you arrive, she is not yet there. Reflecting on your earlier conversation, she probably thought you were <span style='white-space:nowrap'>likeab[l]e.</span>",
"You have just moved to a new area and your neighbor asks if you would like to go to your local bar that evening. When you arrive, she is not yet there. Reflecting on your earlier conversation, she probably thought you were <span style='white-space:nowrap'>d[u]ll.</span>",
"Did you make a bad impression on your new neighbor?"
},
{
"While shopping, you buy a new jacket on the spur of the moment. When trying it on at home, you decide that you do not really like it that much and take it back to the shop. The sales associate gives you a refund and her attitude is <span style='white-space:nowrap'>coopera[t]ive.</span>",
"While shopping, you buy a new jacket on the spur of the moment. When trying it on at home, you decide that you do not really like it that much and take it back to the shop. The sales associate gives you a refund and her attitude is <span style='white-space:nowrap'>relucta[n]t.</span>",
"Was the assistant agreeable when you asked for a refund?"
},
{
"At your computer class you finish your work early and so the teacher gives you a new task to do. You read through it and cannot think how to start so you ask for advice. Your teacher might see you asking for help as a sign of <span style='white-space:nowrap'>com[p]etence.</span>",
"At your computer class you finish your work early and so the teacher gives you a new task to do. You read through it and cannot think how to start so you ask for advice. Your teacher might see you asking for help as a sign of <span style='white-space:nowrap'>weak[n]ess.</span>",
"Was your lecturer understanding when you asked for help?"
},
{
"You organize a holiday party for your friends every year. Last year, it did not go all that well and so you have changed the plans slightly. You anticipate that the problems of the last party will be <span style='white-space:nowrap'>fo[r]gotten.</span>",
"You organize a holiday party for your friends every year. Last year, it did not go all that well and so you have changed the plans slightly. You anticipate that the problems of the last party will be <span style='white-space:nowrap'>repe[a]ted.</span>",
"Do you believe you will have problems with your party again this year?"
},
{
"As a member of a local charity you are asked to promote your fundraising events on local radio. You know that the station is widely listened to and expect that the other committee members will think you spoke <span style='white-space:nowrap'>convinc[i]ngly.</span>",
"As a member of a local charity you are asked to promote your fundraising events on local radio. You know that the station is widely listened to and expect that the other committee members will think you spoke <span style='white-space:nowrap'>hur[r]iedly.</span>",
"Do you think your committee members thought you were a poor speaker?"
},
{
"Some important people are visiting your office and you are asked to present a project to them. On the day of the presentation, you arrange your slides and mentally prepare yourself. You think that your performance will be evaluated as <span style='white-space:nowrap'>org[a]nized.</span>",
"Some important people are visiting your office and you are asked to present a project to them. On the day of the presentation, you arrange your slides and mentally prepare yourself. You think that your performance will be evaluated as <span style='white-space:nowrap'>unp[r]ofessional.</span>",
"Were the visitors disappointed with your performance?"
},
{
"You are given the task of arranging the annual office party. Despite having very little time, you do your best to prepare food, drink, and entertainment. As the night approaches, you think that the event will be a <span style='white-space:nowrap'>succ[e]ss.</span>",
"You are given the task of arranging the annual office party. Despite having very little time, you do your best to prepare food, drink, and entertainment. As the night approaches, you think that the event will be a <span style='white-space:nowrap'>dis[a]ster.</span>",
"Did everyone enjoy the party you planned?"
},
{
"You have been a member of a choir for several years and enjoy performing at concerts. One evening, you are asked to sing on very short notice with another group. Afterwards, you feel that the others found your contribution <span style='white-space:nowrap'>val[u]able.</span>",
"You have been a member of a choir for several years and enjoy performing at concerts. One evening, you are asked to sing on very short notice with another group. Afterwards, you feel that the others found your contribution <span style='white-space:nowrap'>disa[p]pointing.</span>",
"Were the other members of the choir happy with your singing?"
},
{
"You have just moved into a new house and have finally finished the decorating. Your partner invites his/her family over one night to show them your efforts. As they leave, you are sure that his/her mother thought the color scheme was <span style='white-space:nowrap'>tast[e]ful.</span>",
"You have just moved into a new house and have finally finished the decorating. Your partner invites his/her family over one night to show them your efforts. As they leave, you are sure that his/her mother thought the color scheme was <span style='white-space:nowrap'>tast[e]less.</span>",
"Did your partner's mother disapprove of your color scheme?"
},
{
"At your evening class, you are given a task to complete for the next week. You finish it early and ask the tutor for his opinion. He says the work is good, apart from an incomplete section. You feel that he will think you are <span style='white-space:nowrap'>learn[i]ng.</span>",
"At your evening class, you are given a task to complete for the next week. You finish it early and ask the tutor for his opinion. He says the work is good, apart from an incomplete section. You feel that he will think you are <span style='white-space:nowrap'>carel[e]ss.</span>",
"Was your tutor pleased with the quality of work on your paper?"
},
{
"The morning of your first evaluation with your new boss has arrived. She has a reputation for having strong views and as you wait to go in, you think that she might find your work <span style='white-space:nowrap'>satisfacto[r]y.</span>",
"The morning of your first evaluation with your new boss has arrived. She has a reputation for having strong views and as you wait to go in, you think that she might find your work <span style='white-space:nowrap'>uni[n]teresting.</span>",
"Do you think your new boss has a negative impression of you?"
},
{
"You go to a party at a club. While dancing, you spot an old friend not far away and call out. He does not reply and after a moment, turns and leaves the dance floor, heading for the bar. You decide that this is because he was <span style='white-space:nowrap'>dist[r]acted.</span>",
"You go to a party at a club. While dancing, you spot an old friend not far away and call out. He does not reply and after a moment, turns and leaves the dance floor, heading for the bar. You decide that this is because he was <span style='white-space:nowrap'>ir[a]te.</span>",
"Was your friend ignoring you in the club?"
},
{
"When you collect your child from school, you hear some of the parents talking about other people. As you walk closer to them, you overhear your name. When they see you, they smile and greet you. They were probably saying something <span style='white-space:nowrap'>ple[a]sant.</span>",
"When you collect your child from school, you hear some of the parents talking about other people. As you walk closer to them, you overhear your name. When they see you, they smile and greet you. They were probably saying something <span style='white-space:nowrap'>h[u]rtful.</span>",
"Were the parents speaking fondly of you?"
},
{
"Your boss calls a meeting to discuss a new project which will involve most of the staff at your office. You are suddenly asked to contribute your ideas to the discussion. You think that your colleagues will find your ideas <span style='white-space:nowrap'>excit[i]ng.</span>",
"Your boss calls a meeting to discuss a new project which will involve most of the staff at your office. You are suddenly asked to contribute your ideas to the discussion. You think that your colleagues will find your ideas <span style='white-space:nowrap'>b[o]ring.</span>",
"Did your boss dismiss your ideas at the meeting?"
},
{
"You meet someone at a party and feel that you got along well. Later, you call and suggest meeting again. The reply you get makes you feel that the person thinks meeting again would be <span style='white-space:nowrap'>marv[e]lous.</span>",
"You meet someone at a party and feel that you got along well. Later, you call and suggest meeting again. The reply you get makes you feel that the person thinks meeting again would be <span style='white-space:nowrap'>frui[t]less.</span>",
"Did the person from the party accept your invitation for a meeting?"
},
{
"Your boss asks you to do a task at work. You finish it before the deadline, although there is a small mistake in it. You are new to the job and feel that your boss will think you are <span style='white-space:nowrap'>progr[e]ssing.</span>",
"Your boss asks you to do a task at work. You finish it before the deadline, although there is a small mistake in it. You are new to the job and feel that your boss will think you are <span style='white-space:nowrap'>ne[g]ligent.</span>",
"Was your new boss pleased with your performance?"
},
{
"You recently applied to join a local voluntary organization and were sent a questionnaire to fill in. As you finish the last page, you wonder how others may have responded. You decide that in relation to them, you will seem <span style='white-space:nowrap'>des[i]rable.</span>",
"You recently applied to join a local voluntary organization and were sent a questionnaire to fill in. As you finish the last page, you wonder how others may have responded. You decide that in relation to them, you will seem <span style='white-space:nowrap'>med[i]ocre.</span>",
"Were the responses on your questionnaire superior to the others'?"
},
{
"A friend is having problems with her toddler and calls you to discuss the situation. You explain the methods that worked for you when your children were that age. You expect that she will find your advice <span style='white-space:nowrap'>con[s]tructive.</span>",
"A friend is having problems with her toddler and calls you to discuss the situation. You explain the methods that worked for you when your children were that age. You expect that she will find your advice <span style='white-space:nowrap'>impr[a]ctical.</span>",
"Was your friend thankful for your advice?"
},
{
"A new teacher is hired for your history class and you hear that he is very disciplined and hard working. When you meet him for the first time to discuss your interests, you think that he found your work <span style='white-space:nowrap'>thor[o]ugh.</span>",
"A new teacher is hired for your history class and you hear that he is very disciplined and hard working. When you meet him for the first time to discuss your interests, you think that he found your work <span style='white-space:nowrap'>un[i]nspiring.</span>",
"Did your new teacher have a good opinion of your work?"
},
{
"As you are walking down a crowded street, you see your neighbor on the other side. You call out but she does not answer you. You think that this must be because she was <span style='white-space:nowrap'>preoc[c]upied.</span>",
"As you are walking down a crowded street, you see your neighbor on the other side. You call out but she does not answer you. You think that this must be because she was <span style='white-space:nowrap'>cro[s]s.</span>",
"Did your neighbor ignore your call to her in the street?"
},
{
"A new task is assigned to your department at work and your supervisor asks you to be responsible for it. You have no guidelines to follow, and you ask a colleague for advice. Your colleague probably sees this as a sign of <span style='white-space:nowrap'>int[e]lligence.</span>",
"A new task is assigned to your department at work and your supervisor asks you to be responsible for it. You have no guidelines to follow, and you ask a colleague for advice. Your colleague probably sees this as a sign of <span style='white-space:nowrap'>fail[u]re.</span>",
"Did you make a mistake by asking for advice on the new project?"
},
{
"You have just had a new patio deck built in your yard and decide to have a barbecue, as the weather is so nice. As your friends arrive, you can see that they have noticed something different. Their reaction is one of <span style='white-space:nowrap'>app[r]oval.</span>",
"You have just had a new patio deck built in your yard and decide to have a barbecue, as the weather is so nice. As your friends arrive, you can see that they have noticed something different. Their reaction is one of <span style='white-space:nowrap'>dism[a]y.</span>",
"Did your friends have a negative reaction when they saw your new patio?"
},
{
"Recently, you argued with your brother. You decide to break the ice by asking him out for a drink. You get ready and as you are about to leave, he phones to say he can't make it after all. You think that this is because he is feeling <span style='white-space:nowrap'>exhau[s]ted.</span>",
"Recently, you argued with your brother. You decide to break the ice by asking him out for a drink. You get ready and as you are about to leave, he phones to say he can't make it after all. You think that this is because he is feeling <span style='white-space:nowrap'>an[n]oyed.</span>",
"Is your brother upset with you?"
},
{
"You are on the committee of an amateur theater group, which is planning a new production. At the first meeting, the director asks you for ideas about which play to perform. You think that the others will find your suggestions <span style='white-space:nowrap'>commen[d]able.</span>",
"You are on the committee of an amateur theater group, which is planning a new production. At the first meeting, the director asks you for ideas about which play to perform. You think that the others will find your suggestions <span style='white-space:nowrap'>ir[r]elevant.</span>",
"Were your suggestions for the play overlooked by the director?"
},
{
"An opportunity arises for a promotion in your department. You ask for more details of what it will entail. After hearing what would be required of candidates, you decide that if you applied for the job, you would be <span style='white-space:nowrap'>wel[c]omed.</span>",
"An opportunity arises for a promotion in your department. You ask for more details of what it will entail. After hearing what would be required of candidates, you decide that if you applied for the job, you would be <span style='white-space:nowrap'>re[j]ected.</span>",
"Do you think you have a good chance of getting the promotion?"
},
{
"You have been writing to a pen-pal in Belgium for several years and finally arrange for him to come and stay with you. As you stand at the airport waiting for his flight to arrive, you think that his first impression of you will be <span style='white-space:nowrap'>charm[i]ng.</span>",
"You have been writing to a pen-pal in Belgium for several years and finally arrange for him to come and stay with you. As you stand at the airport waiting for his flight to arrive, you think that his first impression of you will be <span style='white-space:nowrap'>un[i]nteresting.</span>",
"Do you make a good impression on your pen-pal at the airport?"
},
{
"Your firm is taking part in a recruitment campaign, and you are asked to go along and speak to local teenagers about the work you do. Other colleagues join you and after your presentation, they tell you that you spoke <span style='white-space:nowrap'>confid[e]ntly.</span>",
"Your firm is taking part in a recruitment campaign, and you are asked to go along and speak to local teenagers about the work you do. Other colleagues join you and after your presentation, they tell you that you spoke <span style='white-space:nowrap'>un[e]asily.</span>",
"Did your presentation to the teenagers go well?"
},
{
"You are required to go to a conference in Scotland for your firm. Your coworker who is supposed to go on the trip falls ill a few days before you leave, so your boss asks someone you don't know to go in his/her place. You think the trip will be <span style='white-space:nowrap'>stim[u]lating.</span>",
"You are required to go to a conference in Scotland for your firm. Your coworker who is supposed to go on the trip falls ill a few days before you leave, so your boss asks someone you don't know to go in his/her place. You think the trip will be <span style='white-space:nowrap'>t[e]nse.</span>",
"Are you looking forward to your business trip with a new colleague?"
},
{
"You have almost completed a computer course and part of your grade will be determined by a presentation that is to be graded by your classmates. You know most of them and feel that this will make them grade your work more <span style='white-space:nowrap'>fav[o]rably.</span>",
"You have almost completed a computer course and part of your grade will be determined by a presentation that is to be graded by your classmates. You know most of them and feel that this will make them grade your work more <span style='white-space:nowrap'>ne[g]atively.</span>",
"Will your classmates grade your presentation advantageously?"
},
{
"Your boss asks to see you following the recent submission of a paper you wrote. He tells you that he wants to talk to you because your work was <span style='white-space:nowrap'>exc[e]ptional.</span>",
"Your boss asks to see you following the recent submission of a paper you wrote. He tells you that he wants to talk to you because your work was <span style='white-space:nowrap'>unc[l]ear.</span>",
"Are you going to get a bad review from your boss?"
},
};

String[][] stimuli2 = {
{
"You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce themselves. After your brief presentation, you guess the others thought you sounded confident.",
"You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce themselves. After your brief presentation, you guess the others thought you sounded <span style='white-space:nowrap'>con[f]ident.</span>",
"You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce themselves. After your brief presentation, you guess the others thought you sounded <span style='white-space:nowrap'>con[f]ide[n]t.</span>",
"You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce themselves. After your brief presentation, you guess the others thought you sounded <span style='white-space:nowrap'>con[f][i]de[n]t.</span>",
"Did you feel dissatisfied with your speech?"
},
{
"A friend suggests that you join an evening class on creative writing. The thought of other people looking at your writing makes you feel enthusiastic.",
"A friend suggests that you join an evening class on creative writing. The thought of other people looking at your writing makes you feel <span style='white-space:nowrap'>enthu[s]iastic.</span>",
"A friend suggests that you join an evening class on creative writing. The thought of other people looking at your writing makes you feel <span style='white-space:nowrap'>enthu[s]iast[i]c.</span>",
"A friend suggests that you join an evening class on creative writing. The thought of other people looking at your writing makes you feel <span style='white-space:nowrap'>enthu[s][i]ast[i]c.</span>",
"Would you expect to feel uncomfortable if others look at your work?"
},
{
"You join a tennis club and before long, you are asked to play in a doubles match. Afterwards you discuss your performance with your partner. Your partner thinks that you played brilliantly.",
"You join a tennis club and before long, you are asked to play in a doubles match. Afterwards you discuss your performance with your partner. Your partner thinks that you played <span style='white-space:nowrap'>br[i]lliantly.</span>",
"You join a tennis club and before long, you are asked to play in a doubles match. Afterwards you discuss your performance with your partner. Your partner thinks that you played <span style='white-space:nowrap'>br[i]lli[a]ntly.</span>",
"You join a tennis club and before long, you are asked to play in a doubles match. Afterwards you discuss your performance with your partner. Your partner thinks that you played <span style='white-space:nowrap'>br[i]ll[i][a]ntly.</span>",
"Did your partner feel pleased with your performance?"
},
{
"Your orchestra asks you to play a solo at the next concert. You practice a few times until you feel ready to play it with the orchestra. At the first rehearsal you make one mistake. The conductor will think that your work is promising.",
"Your orchestra asks you to play a solo at the next concert. You practice a few times until you feel ready to play it with the orchestra. At the first rehearsal you make one mistake. The conductor will think that your work is <span style='white-space:nowrap'>pro[m]ising.</span>",
"Your orchestra asks you to play a solo at the next concert. You practice a few times until you feel ready to play it with the orchestra. At the first rehearsal you make one mistake. The conductor will think that your work is <span style='white-space:nowrap'>pro[m]is[i]ng.</span>",
"Your orchestra asks you to play a solo at the next concert. You practice a few times until you feel ready to play it with the orchestra. At the first rehearsal you make one mistake. The conductor will think that your work is <span style='white-space:nowrap'>pro[m][i]s[i]ng.</span>",
"Did you feel disappointed with your performance?"
},
{
"Your partner asks you to go to an anniversary dinner that his/her company is holding. You have not met any of his/her work colleagues before. Getting ready to go, you think that the new people you will meet will find you friendly.",
"Your partner asks you to go to an anniversary dinner that his/her company is holding. You have not met any of his/her work colleagues before. Getting ready to go, you think that the new people you will meet will find you <span style='white-space:nowrap'>fri[e]ndly.</span>",
"Your partner asks you to go to an anniversary dinner that his/her company is holding. You have not met any of his/her work colleagues before. Getting ready to go, you think that the new people you will meet will find you <span style='white-space:nowrap'>fri[e]nd[l]y.</span>",
"Your partner asks you to go to an anniversary dinner that his/her company is holding. You have not met any of his/her work colleagues before. Getting ready to go, you think that the new people you will meet will find you <span style='white-space:nowrap'>fr[i][e]nd[l]y.</span>",
"Were you disliked by your new acquaintances?"
},
{
"You receive an essay back from your teacher and did not get the grade that you were expecting. She tells you that this is because on this occasion, your work was outstanding.",
"You receive an essay back from your teacher and did not get the grade that you were expecting. She tells you that this is because on this occasion, your work was <span style='white-space:nowrap'>outst[a]nding.</span>",
"You receive an essay back from your teacher and did not get the grade that you were expecting. She tells you that this is because on this occasion, your work was <span style='white-space:nowrap'>outst[a]n[d]ing.</span>",
"You receive an essay back from your teacher and did not get the grade that you were expecting. She tells you that this is because on this occasion, your work was <span style='white-space:nowrap'>ou[t]st[a]n[d]ing.</span>",
"Did you get a better grade than you expected?"
},
{
"You have just moved to a new area and your neighbor asks if you would like to go to your local bar that evening. When you arrive, she is not yet there. Reflecting on your earlier conversation, she probably thought you were likeable.",
"You have just moved to a new area and your neighbor asks if you would like to go to your local bar that evening. When you arrive, she is not yet there. Reflecting on your earlier conversation, she probably thought you were <span style='white-space:nowrap'>likeab[l]e.</span>",
"You have just moved to a new area and your neighbor asks if you would like to go to your local bar that evening. When you arrive, she is not yet there. Reflecting on your earlier conversation, she probably thought you were <span style='white-space:nowrap'>lik[e]ab[l]e.</span>",
"You have just moved to a new area and your neighbor asks if you would like to go to your local bar that evening. When you arrive, she is not yet there. Reflecting on your earlier conversation, she probably thought you were <span style='white-space:nowrap'>lik[e]a[b][l]e.</span>",
"Did you make a bad impression on your new neighbor?"
},
{
"While shopping, you buy a new jacket on the spur of the moment. When trying it on at home, you decide that you do not really like it that much and take it back to the shop. The sales associate gives you a refund and her attitude is cooperative.",
"While shopping, you buy a new jacket on the spur of the moment. When trying it on at home, you decide that you do not really like it that much and take it back to the shop. The sales associate gives you a refund and her attitude is <span style='white-space:nowrap'>coopera[t]ive.</span>",
"While shopping, you buy a new jacket on the spur of the moment. When trying it on at home, you decide that you do not really like it that much and take it back to the shop. The sales associate gives you a refund and her attitude is <span style='white-space:nowrap'>co[o]pera[t]ive.</span>",
"While shopping, you buy a new jacket on the spur of the moment. When trying it on at home, you decide that you do not really like it that much and take it back to the shop. The sales associate gives you a refund and her attitude is <span style='white-space:nowrap'>co[o]pera[t][i]ve.</span>",
"Was the assistant agreeable when you asked for a refund?"
},
{
"At your computer class you finish your work early and so the teacher gives you a new task to do. You read through it and cannot think how to start so you ask for advice. Your teacher might see you asking for help as a sign of competence.",
"At your computer class you finish your work early and so the teacher gives you a new task to do. You read through it and cannot think how to start so you ask for advice. Your teacher might see you asking for help as a sign of <span style='white-space:nowrap'>com[p]etence.</span>",
"At your computer class you finish your work early and so the teacher gives you a new task to do. You read through it and cannot think how to start so you ask for advice. Your teacher might see you asking for help as a sign of <span style='white-space:nowrap'>com[p]ete[n]ce.</span>",
"At your computer class you finish your work early and so the teacher gives you a new task to do. You read through it and cannot think how to start so you ask for advice. Your teacher might see you asking for help as a sign of <span style='white-space:nowrap'>com[p][e]te[n]ce.</span>",
"Was your lecturer understanding when you asked for help?"
},
{
"You organize a holiday party for your friends every year. Last year, it did not go all that well and so you have changed the plans slightly. You anticipate that the problems of the last party will be forgotten.",
"You organize a holiday party for your friends every year. Last year, it did not go all that well and so you have changed the plans slightly. You anticipate that the problems of the last party will be <span style='white-space:nowrap'>fo[r]gotten.</span>",
"You organize a holiday party for your friends every year. Last year, it did not go all that well and so you have changed the plans slightly. You anticipate that the problems of the last party will be <span style='white-space:nowrap'>fo[r]g[o]tten.</span>",
"You organize a holiday party for your friends every year. Last year, it did not go all that well and so you have changed the plans slightly. You anticipate that the problems of the last party will be <span style='white-space:nowrap'>fo[r]g[o][t]ten.</span>",
"Do you believe you will have problems with your party again this year?"
},
{
"As a member of a local charity you are asked to promote your fundraising events on local radio. You know that the station is widely listened to and expect that the other committee members will think you spoke convincingly.",
"As a member of a local charity you are asked to promote your fundraising events on local radio. You know that the station is widely listened to and expect that the other committee members will think you spoke <span style='white-space:nowrap'>convinc[i]ngly.</span>",
"As a member of a local charity you are asked to promote your fundraising events on local radio. You know that the station is widely listened to and expect that the other committee members will think you spoke <span style='white-space:nowrap'>conv[i]nc[i]ngly.</span>",
"As a member of a local charity you are asked to promote your fundraising events on local radio. You know that the station is widely listened to and expect that the other committee members will think you spoke <span style='white-space:nowrap'>conv[i]nc[i][n]gly.</span>",
"Do you think your committee members thought you were a poor speaker?"
},
{
"Some important people are visiting your office and you are asked to present a project to them. On the day of the presentation, you arrange your slides and mentally prepare yourself. You think that your performance will be evaluated as organized.",
"Some important people are visiting your office and you are asked to present a project to them. On the day of the presentation, you arrange your slides and mentally prepare yourself. You think that your performance will be evaluated as <span style='white-space:nowrap'>org[a]nized.</span>",
"Some important people are visiting your office and you are asked to present a project to them. On the day of the presentation, you arrange your slides and mentally prepare yourself. You think that your performance will be evaluated as <span style='white-space:nowrap'>org[a]niz[e]d.</span>",
"Some important people are visiting your office and you are asked to present a project to them. On the day of the presentation, you arrange your slides and mentally prepare yourself. You think that your performance will be evaluated as <span style='white-space:nowrap'>org[a]n[i]z[e]d.</span>",
"Were the visitors disappointed with your performance?"
},
{
"You are given the task of arranging the annual office party. Despite having very little time, you do your best to prepare food, drink, and entertainment. As the night approaches, you think that the event will be a success.",
"You are given the task of arranging the annual office party. Despite having very little time, you do your best to prepare food, drink, and entertainment. As the night approaches, you think that the event will be a <span style='white-space:nowrap'>succ[e]ss.</span>",
"You are given the task of arranging the annual office party. Despite having very little time, you do your best to prepare food, drink, and entertainment. As the night approaches, you think that the event will be a <span style='white-space:nowrap'>suc[c][e]ss.</span>",
"You are given the task of arranging the annual office party. Despite having very little time, you do your best to prepare food, drink, and entertainment. As the night approaches, you think that the event will be a <span style='white-space:nowrap'>suc[c][e][s]s.</span>",
"Did everyone enjoy the party you planned?"
},
{
"You have been a member of a choir for several years and enjoy performing at concerts. One evening, you are asked to sing on very short notice with another group. Afterwards, you feel that the others found your contribution valuable.",
"You have been a member of a choir for several years and enjoy performing at concerts. One evening, you are asked to sing on very short notice with another group. Afterwards, you feel that the others found your contribution <span style='white-space:nowrap'>val[u]able.</span>",
"You have been a member of a choir for several years and enjoy performing at concerts. One evening, you are asked to sing on very short notice with another group. Afterwards, you feel that the others found your contribution <span style='white-space:nowrap'>val[u]ab[l]e.</span>",
"You have been a member of a choir for several years and enjoy performing at concerts. One evening, you are asked to sing on very short notice with another group. Afterwards, you feel that the others found your contribution <span style='white-space:nowrap'>va[l][u]ab[l]e.</span>",
"Were the other members of the choir happy with your singing?"
},
{
"You have just moved into a new house and have finally finished the decorating. Your partner invites his/her family over one night to show them your efforts. As they leave, you are sure that his/her mother thought the color scheme was tasteful.",
"You have just moved into a new house and have finally finished the decorating. Your partner invites his/her family over one night to show them your efforts. As they leave, you are sure that his/her mother thought the color scheme was <span style='white-space:nowrap'>tast[e]ful.</span>",
"You have just moved into a new house and have finally finished the decorating. Your partner invites his/her family over one night to show them your efforts. As they leave, you are sure that his/her mother thought the color scheme was <span style='white-space:nowrap'>tas[t][e]ful.</span>",
"You have just moved into a new house and have finally finished the decorating. Your partner invites his/her family over one night to show them your efforts. As they leave, you are sure that his/her mother thought the color scheme was <span style='white-space:nowrap'>tas[t][e]f[u]l.</span>",
"Did your partner's mother disapprove of your color scheme?"
},
{
"At your evening class, you are given a task to complete for the next week. You finish it early and ask the tutor for his opinion. He says the work is good, apart from an incomplete section. You feel that he will think you are learning.",
"At your evening class, you are given a task to complete for the next week. You finish it early and ask the tutor for his opinion. He says the work is good, apart from an incomplete section. You feel that he will think you are <span style='white-space:nowrap'>learn[i]ng.</span>",
"At your evening class, you are given a task to complete for the next week. You finish it early and ask the tutor for his opinion. He says the work is good, apart from an incomplete section. You feel that he will think you are <span style='white-space:nowrap'>learn[i][n]g.</span>",
"At your evening class, you are given a task to complete for the next week. You finish it early and ask the tutor for his opinion. He says the work is good, apart from an incomplete section. You feel that he will think you are <span style='white-space:nowrap'>le[a]rn[i][n]g.</span>",
"Was your tutor pleased with the quality of work on your paper?"
},
{
"The morning of your first evaluation with your new boss has arrived. She has a reputation for having strong views and as you wait to go in, you think that she might find your work satisfactory.",
"The morning of your first evaluation with your new boss has arrived. She has a reputation for having strong views and as you wait to go in, you think that she might find your work <span style='white-space:nowrap'>satisfacto[r]y.</span>",
"The morning of your first evaluation with your new boss has arrived. She has a reputation for having strong views and as you wait to go in, you think that she might find your work <span style='white-space:nowrap'>satis[f]acto[r]y.</span>",
"The morning of your first evaluation with your new boss has arrived. She has a reputation for having strong views and as you wait to go in, you think that she might find your work <span style='white-space:nowrap'>sa[t]is[f]acto[r]y.</span>",
"Does your new boss dislike your work?"
},
{
"You go to a party at a club. While dancing, you spot an old friend not far away and call out. He does not reply and after a moment, turns and leaves the dance floor, heading for the bar. You decide that this is because he was distracted.",
"You go to a party at a club. While dancing, you spot an old friend not far away and call out. He does not reply and after a moment, turns and leaves the dance floor, heading for the bar. You decide that this is because he was <span style='white-space:nowrap'>dist[r]acted.</span>",
"You go to a party at a club. While dancing, you spot an old friend not far away and call out. He does not reply and after a moment, turns and leaves the dance floor, heading for the bar. You decide that this is because he was <span style='white-space:nowrap'>d[i]st[r]acted.</span>",
"You go to a party at a club. While dancing, you spot an old friend not far away and call out. He does not reply and after a moment, turns and leaves the dance floor, heading for the bar. You decide that this is because he was <span style='white-space:nowrap'>d[i]st[r]act[e]d.</span>",
"Was your friend ignoring you in the club?"
},
{
"When you collect your child from school, you hear some of the parents talking about other people. As you walk closer to them, you overhear your name. When they see you, they smile and greet you. They were probably saying something pleasant.",
"When you collect your child from school, you hear some of the parents talking about other people. As you walk closer to them, you overhear your name. When they see you, they smile and greet you. They were probably saying something <span style='white-space:nowrap'>ple[a]sant.</span>",
"When you collect your child from school, you hear some of the parents talking about other people. As you walk closer to them, you overhear your name. When they see you, they smile and greet you. They were probably saying something <span style='white-space:nowrap'>ple[a]sa[n]t.</span>",
"When you collect your child from school, you hear some of the parents talking about other people. As you walk closer to them, you overhear your name. When they see you, they smile and greet you. They were probably saying something <span style='white-space:nowrap'>pl[e][a]sa[n]t.</span>",
"Were the parents speaking fondly of you?"
},
{
"Your boss calls a meeting to discuss a new project which will involve most of the staff at your office. You are suddenly asked to contribute your ideas to the discussion. You think that your colleagues will find your ideas exciting.",
"Your boss calls a meeting to discuss a new project which will involve most of the staff at your office. You are suddenly asked to contribute your ideas to the discussion. You think that your colleagues will find your ideas <span style='white-space:nowrap'>excit[i]ng.</span>",
"Your boss calls a meeting to discuss a new project which will involve most of the staff at your office. You are suddenly asked to contribute your ideas to the discussion. You think that your colleagues will find your ideas <span style='white-space:nowrap'>exc[i]t[i]ng.</span>",
"Your boss calls a meeting to discuss a new project which will involve most of the staff at your office. You are suddenly asked to contribute your ideas to the discussion. You think that your colleagues will find your ideas <span style='white-space:nowrap'>ex[c][i]t[i]ng.</span>",
"Did your boss dismiss your ideas at the meeting?"
},
{
"You meet someone at a party and feel that you got along well. Later, you call and suggest meeting again. The reply you get makes you feel that the person thinks meeting again would be marvelous.",
"You meet someone at a party and feel that you got along well. Later, you call and suggest meeting again. The reply you get makes you feel that the person thinks meeting again would be <span style='white-space:nowrap'>marv[e]lous.</span>",
"You meet someone at a party and feel that you got along well. Later, you call and suggest meeting again. The reply you get makes you feel that the person thinks meeting again would be <span style='white-space:nowrap'>marv[e]l[o]us.</span>",
"You meet someone at a party and feel that you got along well. Later, you call and suggest meeting again. The reply you get makes you feel that the person thinks meeting again would be <span style='white-space:nowrap'>ma[r]v[e]l[o]us.</span>",
"Did the person from the party accept your invitation for a meeting?"
},
{
"Your boss asks you to do a task at work. You finish it before the deadline, although there is a small mistake in it. You are new to the job and feel that your boss will think you are progressing.",
"Your boss asks you to do a task at work. You finish it before the deadline, although there is a small mistake in it. You are new to the job and feel that your boss will think you are <span style='white-space:nowrap'>progr[e]ssing.</span>",
"Your boss asks you to do a task at work. You finish it before the deadline, although there is a small mistake in it. You are new to the job and feel that your boss will think you are <span style='white-space:nowrap'>progr[e]ss[i]ng.</span>",
"Your boss asks you to do a task at work. You finish it before the deadline, although there is a small mistake in it. You are new to the job and feel that your boss will think you are <span style='white-space:nowrap'>progr[e]ss[i][n]g.</span>",
"Was your new boss pleased with your performance?"
},
{
"You recently applied to join a local voluntary organization and were sent a questionnaire to fill in. As you finish the last page, you wonder how others may have responded. You decide that in relation to them, you will seem desirable.",
"You recently applied to join a local voluntary organization and were sent a questionnaire to fill in. As you finish the last page, you wonder how others may have responded. You decide that in relation to them, you will seem <span style='white-space:nowrap'>des[i]rable.</span>",
"You recently applied to join a local voluntary organization and were sent a questionnaire to fill in. As you finish the last page, you wonder how others may have responded. You decide that in relation to them, you will seem <span style='white-space:nowrap'>des[i]r[a]ble.</span>",
"You recently applied to join a local voluntary organization and were sent a questionnaire to fill in. As you finish the last page, you wonder how others may have responded. You decide that in relation to them, you will seem <span style='white-space:nowrap'>des[i]r[a]b[l]e.</span>",
"Were the responses on your questionnaire superior to the others'?"
},
{
"A friend is having problems with her toddler and calls you to discuss the situation. You explain the methods that worked for you when your children were that age. You expect that she will find your advice constructive.",
"A friend is having problems with her toddler and calls you to discuss the situation. You explain the methods that worked for you when your children were that age. You expect that she will find your advice <span style='white-space:nowrap'>con[s]tructive.</span>",
"A friend is having problems with her toddler and calls you to discuss the situation. You explain the methods that worked for you when your children were that age. You expect that she will find your advice <span style='white-space:nowrap'>con[s]truct[i]ve.</span>",
"A friend is having problems with her toddler and calls you to discuss the situation. You explain the methods that worked for you when your children were that age. You expect that she will find your advice <span style='white-space:nowrap'>con[s]tr[u]ct[i]ve.</span>",
"Was your friend thankful for your advice?"
},
{
"A new teacher is hired for your history class and you hear that he is very disciplined and hard working. When you meet him for the first time to discuss your interests, you think that he found your work thorough.",
"A new teacher is hired for your history class and you hear that he is very disciplined and hard working. When you meet him for the first time to discuss your interests, you think that he found your work <span style='white-space:nowrap'>thor[o]ugh.</span>",
"A new teacher is hired for your history class and you hear that he is very disciplined and hard working. When you meet him for the first time to discuss your interests, you think that he found your work <span style='white-space:nowrap'>thor[o][u]gh.</span>",
"A new teacher is hired for your history class and you hear that he is very disciplined and hard working. When you meet him for the first time to discuss your interests, you think that he found your work <span style='white-space:nowrap'>thor[o][u][g]h.</span>",
"Did your new teacher have a good opinion of your work?"
},
{
"As you are walking down a crowded street, you see your neighbor on the other side. You call out but she does not answer you. You think that this must be because she was preoccupied.",
"As you are walking down a crowded street, you see your neighbor on the other side. You call out but she does not answer you. You think that this must be because she was <span style='white-space:nowrap'>preoc[c]upied.</span>",
"As you are walking down a crowded street, you see your neighbor on the other side. You call out but she does not answer you. You think that this must be because she was <span style='white-space:nowrap'>pre[o]c[c]upied.</span>",
"As you are walking down a crowded street, you see your neighbor on the other side. You call out but she does not answer you. You think that this must be because she was <span style='white-space:nowrap'>pr[e][o]c[c]upied.</span>",
"Did your neighbor ignore your call to her in the street?"
},
{
"A new task is assigned to your department at work and your supervisor asks you to be responsible for it. You have no guidelines to follow, and you ask a colleague for advice. Your colleague probably sees this as a sign of intelligence.",
"A new task is assigned to your department at work and your supervisor asks you to be responsible for it. You have no guidelines to follow, and you ask a colleague for advice. Your colleague probably sees this as a sign of <span style='white-space:nowrap'>int[e]lligence.</span>",
"A new task is assigned to your department at work and your supervisor asks you to be responsible for it. You have no guidelines to follow, and you ask a colleague for advice. Your colleague probably sees this as a sign of <span style='white-space:nowrap'>int[e]l[l]igence.</span>",
"A new task is assigned to your department at work and your supervisor asks you to be responsible for it. You have no guidelines to follow, and you ask a colleague for advice. Your colleague probably sees this as a sign of <span style='white-space:nowrap'>in[t][e]l[l]igence.</span>",
"Did you make a mistake by asking for advice on the new project?"
},
{
"You have just had a new patio deck built in your yard and decide to have a barbecue, as the weather is so nice. As your friends arrive, you can see that they have noticed something different. Their reaction is one of approval.",
"You have just had a new patio deck built in your yard and decide to have a barbecue, as the weather is so nice. As your friends arrive, you can see that they have noticed something different. Their reaction is one of <span style='white-space:nowrap'>app[r]oval.</span>",
"You have just had a new patio deck built in your yard and decide to have a barbecue, as the weather is so nice. As your friends arrive, you can see that they have noticed something different. Their reaction is one of <span style='white-space:nowrap'>app[r]ov[a]l.</span>",
"You have just had a new patio deck built in your yard and decide to have a barbecue, as the weather is so nice. As your friends arrive, you can see that they have noticed something different. Their reaction is one of <span style='white-space:nowrap'>app[r][o]v[a]l.</span>",
"Did your friends have a negative reaction when they saw your new patio?"
},
{
"Recently, you argued with your brother. You decide to break the ice by asking him out for a drink. You get ready and as you are about to leave, he phones to say he can't make it after all. You think that this is because he is feeling exhausted.",
"Recently, you argued with your brother. You decide to break the ice by asking him out for a drink. You get ready and as you are about to leave, he phones to say he can't make it after all. You think that this is because he is feeling <span style='white-space:nowrap'>exhau[s]ted.</span>",
"Recently, you argued with your brother. You decide to break the ice by asking him out for a drink. You get ready and as you are about to leave, he phones to say he can't make it after all. You think that this is because he is feeling <span style='white-space:nowrap'>ex[h]au[s]ted.</span>",
"Recently, you argued with your brother. You decide to break the ice by asking him out for a drink. You get ready and as you are about to leave, he phones to say he can't make it after all. You think that this is because he is feeling <span style='white-space:nowrap'>ex[h]au[s]t[e]d.</span>",
"Is your brother upset with you?"
},
{
"You are on the committee of an amateur theater group, which is planning a new production. At the first meeting, the director asks you for ideas about which play to perform. You think that the others will find your suggestions commendable.",
"You are on the committee of an amateur theater group, which is planning a new production. At the first meeting, the director asks you for ideas about which play to perform. You think that the others will find your suggestions <span style='white-space:nowrap'>commen[d]able.</span>",
"You are on the committee of an amateur theater group, which is planning a new production. At the first meeting, the director asks you for ideas about which play to perform. You think that the others will find your suggestions <span style='white-space:nowrap'>commen[d]abl[e].</span>",
"You are on the committee of an amateur theater group, which is planning a new production. At the first meeting, the director asks you for ideas about which play to perform. You think that the others will find your suggestions <span style='white-space:nowrap'>commen[d][a]bl[e].</span>",
"Were your suggestions for the play overlooked by the director?"
},
{
"An opportunity arises for a promotion in your department. You ask for more details of what it will entail. After hearing what would be required of candidates, you decide that if you applied for the job, you would be welcomed.",
"An opportunity arises for a promotion in your department. You ask for more details of what it will entail. After hearing what would be required of candidates, you decide that if you applied for the job, you would be <span style='white-space:nowrap'>wel[c]omed.</span>",
"An opportunity arises for a promotion in your department. You ask for more details of what it will entail. After hearing what would be required of candidates, you decide that if you applied for the job, you would be <span style='white-space:nowrap'>wel[c]om[e]d.</span>",
"An opportunity arises for a promotion in your department. You ask for more details of what it will entail. After hearing what would be required of candidates, you decide that if you applied for the job, you would be <span style='white-space:nowrap'>wel[c]o[m][e]d.</span>",
"Do you think you have a good chance of getting the promotion?"
},
{
"You have been writing to a pen-pal in Belgium for several years and finally arrange for him to come and stay with you. As you stand at the airport waiting for his flight to arrive, you think that his first impression of you will be charming.",
"You have been writing to a pen-pal in Belgium for several years and finally arrange for him to come and stay with you. As you stand at the airport waiting for his flight to arrive, you think that his first impression of you will be <span style='white-space:nowrap'>charm[i]ng.</span>",
"You have been writing to a pen-pal in Belgium for several years and finally arrange for him to come and stay with you. As you stand at the airport waiting for his flight to arrive, you think that his first impression of you will be <span style='white-space:nowrap'>charm[i][n]g.</span>",
"You have been writing to a pen-pal in Belgium for several years and finally arrange for him to come and stay with you. As you stand at the airport waiting for his flight to arrive, you think that his first impression of you will be <span style='white-space:nowrap'>cha[r]m[i][n]g.</span>",
"Do you make a good impression on your pen-pal at the airport?"
},
{
"Your firm is taking part in a recruitment campaign, and you are asked to go along and speak to local teenagers about the work you do. Other colleagues join you and after your presentation, they tell you that you spoke confidently.",
"Your firm is taking part in a recruitment campaign, and you are asked to go along and speak to local teenagers about the work you do. Other colleagues join you and after your presentation, they tell you that you spoke <span style='white-space:nowrap'>confid[e]ntly.</span>",
"Your firm is taking part in a recruitment campaign, and you are asked to go along and speak to local teenagers about the work you do. Other colleagues join you and after your presentation, they tell you that you spoke <span style='white-space:nowrap'>conf[i]d[e]ntly.</span>",
"Your firm is taking part in a recruitment campaign, and you are asked to go along and speak to local teenagers about the work you do. Other colleagues join you and after your presentation, they tell you that you spoke <span style='white-space:nowrap'>conf[i]d[e][n]tly.</span>",
"Did your presentation to the teenagers go well?"
},
{
"You are required to go to a conference in Scotland for your firm. Your coworker who is supposed to go on the trip falls ill a few days before you leave, so your boss asks someone you don't know to go in his/her place. You think the trip will be stimulating.",
"You are required to go to a conference in Scotland for your firm. Your coworker who is supposed to go on the trip falls ill a few days before you leave, so your boss asks someone you don't know to go in his/her place. You think the trip will be <span style='white-space:nowrap'>stim[u]lating.</span>",
"You are required to go to a conference in Scotland for your firm. Your coworker who is supposed to go on the trip falls ill a few days before you leave, so your boss asks someone you don't know to go in his/her place. You think the trip will be <span style='white-space:nowrap'>stim[u]lat[i]ng.</span>",
"You are required to go to a conference in Scotland for your firm. Your coworker who is supposed to go on the trip falls ill a few days before you leave, so your boss asks someone you don't know to go in his/her place. You think the trip will be <span style='white-space:nowrap'>stim[u]l[a]t[i]ng.</span>",
"Are you looking forward to your business trip with a new colleague?"
},
{
"You have almost completed a computer course and part of your grade will be determined by a presentation that is to be graded by your classmates. You know most of them and feel that this will make them grade your work more favorably.",
"You have almost completed a computer course and part of your grade will be determined by a presentation that is to be graded by your classmates. You know most of them and feel that this will make them grade your work more <span style='white-space:nowrap'>fav[o]rably.</span>",
"You have almost completed a computer course and part of your grade will be determined by a presentation that is to be graded by your classmates. You know most of them and feel that this will make them grade your work more <span style='white-space:nowrap'>fa[v][o]rably.</span>",
"You have almost completed a computer course and part of your grade will be determined by a presentation that is to be graded by your classmates. You know most of them and feel that this will make them grade your work more <span style='white-space:nowrap'>fa[v][o]rab[l]y.</span>",
"Will your classmates grade your presentation advantageously?"
},
{
"Your boss asks to see you following the recent submission of a paper you wrote. He tells you that he wants to talk to you because your work was exceptional.",
"Your boss asks to see you following the recent submission of a paper you wrote. He tells you that he wants to talk to you because your work was <span style='white-space:nowrap'>exc[e]ptional.</span>",
"Your boss asks to see you following the recent submission of a paper you wrote. He tells you that he wants to talk to you because your work was <span style='white-space:nowrap'>exc[e]pt[i]onal.</span>",
"Your boss asks to see you following the recent submission of a paper you wrote. He tells you that he wants to talk to you because your work was <span style='white-space:nowrap'>exc[e]pt[i]o[n]al.</span>",
"Are you going to get a bad review from your boss?"
},
};


int[] orineganswers = {1,1,2,1,  1,2,1,2,   2,1,1,1,   2,2,1,2,   1,1,2,1,    2,2,2,2,    2,1,1,1,   1,1,2,2,   2,2,2,1} ;

int[] neganswers = new int[72];

for (int i=0;i<36;i++) {
  int j=i*2;
  neganswers[j]=orineganswers[i];
  neganswers[j+1]=orineganswers[i];
  }

String[][][] ratings =
{
{{"ratings"},{"ncols","1"},{"flow","horizontal"},{"direction","left"}},
{{"next"},{"options","Next"}},
{{"ysno"},{"options","Yes","No"}}
};


String[][][]  frag = {
{{"frag"}, {"nelem","72"},{"musthave","0"},{"group","0"},{"gminmax","72.72"},{"gplace","0"},{"gorder","0"},{"gsequence","fixed"},{"prefix",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}},
{{""}, {"choices","next"},{"stem",""}}, 
{{""}, {"choices","ysno"},{"stem",""}}
};


Random generator = new Random(); 

int mycondition = generator.nextInt(5)+1;

String instructions="";


if (mycondition==2) 
instructions="In this task, you will see a series of paragraphs and questions. Please read each paragraph carefully, and <b>imagine yourself in the situations described</b>. When you have finished, click &#8220;Next&#8221; twice. After reading the situation, you will be asked to answer a question about the situation in which you imagined yourself. Please use only the information from the situation to answer the question. Once you have the correct answer, you will move onto the next situation. If you are unsure about an item, please make your best guess.";
    else 
 instructions = "In this task, you will see a series of paragraphs and questions. Please read each paragraph carefully, and <b>imagine yourself in the situations described</b>. At the end of each paragraph, there will be a word fragment (an incomplete word) for you to complete. To complete the word fragment, move the cursor to where a letter is missing, press the keys that correspond to the missing letters, and then click &#8220;Next&#8221; twice. Once you type in the correct letter, you will move onto the next question. If you type an incorrect letter three times in a row, you will also move to the next question. After completing the word fragment, you will be asked to answer a question about the situation in which you imagined yourself. Please use only the information from the situation to answer the question. Once you have the correct answer, you will move onto the next situation. If you are unsure about an item, please make your best guess.";


if (mycondition==1) {
ArrayList <Integer> posneg = new ArrayList <Integer> ();
for (int i=0;i<18;i++) posneg.add(0);
for (int i=0;i<18;i++) posneg.add(1);
Collections.shuffle(posneg);

for (int i=1;i<=36;i++) {
  int j=(i-1)*2+1;
  frag[j][0][0]="frg"+i;
  frag[j][2][1]=stimuli1[i-1][posneg.get(i-1)];
  if (posneg.get(i-1)==0) frag[j+1][0][0]="pst"+i;
  if (posneg.get(i-1)==1) frag[j+1][0][0]="ngt"+i;
  frag[j+1][2][1]=stimuli1[i-1][2];
  }
}

if (mycondition==2) {
for (int i=1;i<=36;i++) {
  int j=(i-1)*2+1;
  frag[j][0][0]="frg"+i;
  frag[j][2][1]=stimuli2[i-1][0];
  frag[j+1][0][0]="pst"+i;
  frag[j+1][2][1]=stimuli2[i-1][4];
  }
}

if (mycondition==3) {
for (int i=1;i<=36;i++) {
  int j=(i-1)*2+1;
  frag[j][0][0]="frg"+i;
  frag[j][2][1]=stimuli2[i-1][1];
  frag[j+1][0][0]="pst"+i;
  frag[j+1][2][1]=stimuli2[i-1][4];
  }
}

if (mycondition==4) {
for (int i=1;i<=36;i++) {
  int j=(i-1)*2+1;
  frag[j][0][0]="frg"+i;
  frag[j][2][1]=stimuli2[i-1][2];
  frag[j+1][0][0]="pst"+i;
  frag[j+1][2][1]=stimuli2[i-1][4];
  }
}

if (mycondition==5) {
for (int i=1;i<=36;i++) {
  int j=(i-1)*2+1;
  frag[j][0][0]="frg"+i;
  frag[j][2][1]=stimuli2[i-1][3];
  frag[j+1][0][0]="pst"+i;
  frag[j+1][2][1]=stimuli2[i-1][4];
  }
}



// now we need to shuffle the items for sequencing

int nselect = 72 ;  
int beginshuffle=1;  int endshuffle=72;  
int nshufflepairs =  (endshuffle - beginshuffle + 1)/2 ;  // this should give us 36 pairs; 

int nswap=0; 
int x, y,z;
String tstem="",tid="",tchoices="";
while (nswap<=500) {
  nswap++;
  x=generator.nextInt(nshufflepairs)*2+beginshuffle;
  y=generator.nextInt(nshufflepairs)*2+beginshuffle;
  
  z = neganswers[x-1];neganswers[x-1]=neganswers[y-1];neganswers[y-1]=z;
  tid=frag[x][0][0];  tchoices=frag[x][1][1];  tstem=frag[x][2][1];
  frag[x][0][0]=frag[y][0][0]; frag[x][1][1]=frag[y][1][1]; frag[x][2][1]=frag[y][2][1];
  frag[y][0][0]=tid; frag[y][1][1]=tchoices; frag[y][2][1]=tstem;
  
  x++; y++; // these are the indices of the follow-up question
  z = neganswers[x-1];neganswers[x-1]=neganswers[y-1];neganswers[y-1]=z;
  tid=frag[x][0][0];  tchoices=frag[x][1][1];  tstem=frag[x][2][1];
  frag[x][0][0]=frag[y][0][0]; frag[x][1][1]=frag[y][1][1]; frag[x][2][1]=frag[y][2][1];
  frag[y][0][0]=tid; frag[y][1][1]=tchoices; frag[y][2][1]=tstem;
 }



 ArrayList <String[][][]> scales = new ArrayList <String[][][]> ();
 scales.add(frag); 
 int nscales= scales.size(); 


%>

<%!

public class index implements Comparable<index> {
  public int place=0;
  public int value=0;
  
  public int compareTo (index b) {
  
   if (value<b.value) return -1;
   if (value==b.value) return 0;
   if (value>b.value) return  1;
   
   return 0;
  }

}


public class item  {
  public int nitems=0;
  public ArrayList <String> flow = new ArrayList<String>();
  public ArrayList <String> ncols = new ArrayList<String>();
  public ArrayList <String> id = new ArrayList<String>();
  public ArrayList <String> prefix = new ArrayList<String>();
  public ArrayList <String> precheck = new ArrayList<String>();
  public ArrayList <String> stem = new ArrayList<String>();
  public ArrayList <String> bwidth = new ArrayList<String>();
  public ArrayList <String> minselect = new ArrayList<String>();
  public ArrayList <String> maxselect = new ArrayList<String>();
  public ArrayList <ArrayList<String>> values = new ArrayList<ArrayList<String>>();
  public ArrayList <ArrayList<String>> options = new ArrayList<ArrayList<String>>();
     
}

 void printmap (HashMap<String,ArrayList<String>> h) {
      System.out.println();
       for (String s: h.keySet()) 
         System.out.println(s+h.get(s).toString());
       System.out.println();
 
 }
 

  void  insertmapvalue (HashMap<String,ArrayList<String>> h, String key,String value) {
    ArrayList<String> t = new ArrayList<String> ();
    t.add(value);
    h.put(key,t);
   }
   
ArrayList<String> getkeysinstring (String s, char c) {

		ArrayList<String> as = new ArrayList<String>();
		int sindex=s.indexOf(c);
		int eindex=s.indexOf(c,sindex+1);
		while ((sindex >= 0) && (eindex>(sindex+1))) {
			as.add(s.substring(sindex+1,eindex));
			sindex=s.indexOf(c,eindex+1);
			if (sindex>0) eindex=s.indexOf(c,sindex+1);
		}
		
	//if (s.indexOf("@label")>=0) p(s+"-->"+as.toString());
	
	return as;
		

	}
	
	String joinstrings (ArrayList<String> as, String d) {
		String s= new String("");
		if ((as!=null) && (as.size()>0)) { 
		    for (int i=0;i<as.size()-1;i++) s+=as.get(i)+d;
            s+=as.get(as.size()-1); 
           }
		return s;
	}
	
	
	
	//tricky buggg.. needed to prepend the dot in the indexOf
	
	ArrayList<String> selectmapvalue (String tvk, ArrayList<String> listofkeys, HashMap<String,ArrayList<String>> mymap) {
	
	 ArrayList<String> t = new ArrayList<String> ();
	  
	 // non default search
	 for (String key:listofkeys) 
        if ( ((key.indexOf("."+tvk)+tvk.length()+1)==key.length()) && !(key.indexOf(".all.")>0)) 
          return mymap.get(key);
     
     // default search
     for (String key:listofkeys) 
        if ( ((key.indexOf("."+tvk)+tvk.length()+1)==key.length()) && (key.indexOf(".all.")>0)) 
          return mymap.get(key);
          
     // return nothing
     
     return t;
     
    
    }
    
                    

void p(String s) {
System.out.println(s);
}

void p(int s) {
System.out.println(s);
}


HashMap<String, ArrayList<String>> map2d (HashMap<String,ArrayList<String>> context, String[][][] structure) {
  return map2d(context,structure,null);
}

HashMap<String, ArrayList<String>> map2d (HashMap<String,ArrayList<String>> context, String[][][] structure, String selectedrow) {

		String ts="";
		String sep=".";
		String aprefix="";
		String iprefix="";
		String defprefix=context.get("selfid").get(0);
		int numrows=0;
		ArrayList <String> contextkeys = new ArrayList <String> ();
		
		
		HashMap<String,ArrayList<String>> contents = new HashMap<String, ArrayList<String>> ();  

        // this ensures that the arraylist values in contents can be modified without 
        // modifying the original context parameter.. however at the end, these context values are removed
        
        
        for ( String key : context.keySet()) 
         if (!key.equals("selfid"))
        {
             ArrayList<String> ta = new ArrayList<String>();
             for (String s:context.get(key)) ta.add(s);
             contents.put(key,ta);
             contextkeys.add(key);
         }
         
       
       insertmapvalue(contents,"selfid",structure[0][0][0]);
       
            
       for (int q=0;q<structure.length;q++)  {
       
            if (q==0) iprefix=defprefix; else iprefix=structure[0][0][0];
            
            if (q==0) aprefix=structure[0][0][0]+".all"; 
				else aprefix=structure[0][0][0]+"."+structure[q][0][0];
            
            
            ArrayList<String> tkeykey = getkeysinstring(aprefix,'@');
            
            
            for (String  tvk:tkeykey) {
    			String sconcat = joinstrings(selectmapvalue(tvk,contextkeys,contents),"");
                aprefix=aprefix.replace("@"+tvk+"@",sconcat); // gets rid of the prefix
			}
				
			
			if ((q==0) || (selectedrow==null) || ((selectedrow!=null) && selectedrow.equals(aprefix))) {          
            if (q>0) numrows++;
                       
            if (q>0) insertmapvalue(contents,iprefix+".rowid."+Integer.toString(numrows),aprefix);
            if (q>0) insertmapvalue(contents,iprefix+".all.numrows",Integer.toString(numrows));
            
			
                
        for (int k=1;k<structure[q].length;k++) {
             
             ArrayList<String> tkeys = getkeysinstring(structure[q][k][0],'@');
             ArrayList<String> tkeylist = new ArrayList<String>();
             
             
             if (tkeys.size()==0) tkeylist.add(structure[q][k][0]);
				else for (String key:tkeys) 
				 {
	              tkeylist.addAll(selectmapvalue(key,contextkeys,contents));
                  }
             ArrayList<String> tvaluelist = new ArrayList<String>();
          
              
             int first=0;int second=0;
			 for (int m=1;m<structure[q][k].length;m++) {
         
                ts=structure[q][k][m];
               
                ArrayList<String> tvaluekeys = getkeysinstring(ts,'@');
                ArrayList<String> tarr = new ArrayList<String>();
                
                first=ts.indexOf('@');
                if (first>=0) second=ts.indexOf('@',first+1);else second=-1;
                
           
                if ((first==0) && (second==ts.length()-1)) 
                {
                  tarr=selectmapvalue(tvaluekeys.get(0),contextkeys,contents);
                
                }
                 
                 else {
                 
                   first=ts.indexOf('['); second=ts.indexOf(']');
                   if ((first==0) && (second==ts.length()-1)) {
                   String st = ts.substring(1,ts.length()-1);
                   String[] rar = st.split("\\.\\.");
                   int start = Integer.parseInt(rar[0]);
                   int end = Integer.parseInt(rar[1]);
                   int nval=(int) Math.abs(start-end)+1; 
                   int cval=0; int count=0; count=start; 
                   while (cval<nval) {
					 tarr.add(new Integer(count).toString());
					 if (start<end) count++; else count--;
					 cval++;
                     
                   }
                 } 
                 
                  else
                 if (tvaluekeys.size()>0) {
                for (String  tvk:tvaluekeys) {
                    String sconcat = joinstrings(selectmapvalue(tvk,contextkeys,contents),""); 
                    ts=ts.replace("@"+tvk+"@",sconcat); // gets rid of the iprefix
				 }
				 
                	tarr.add(ts);  
				
                }
			} 
			
			 if  (tarr.size()==0) tarr.add(ts);
			 tvaluelist.addAll(tarr);
               
           } 
             
            
             
             //single key case
             if (tkeylist.size()==1) 
                contents.put(aprefix+sep+tkeylist.get(0),tvaluelist);
             
             //multiple key case
              else if ((tkeylist.size()>1) && (tvaluelist.size()==tkeylist.size())) 
                for (int i=0;i<tkeylist.size();i++) { 
                  ArrayList<String> ta = new ArrayList<String>();
                  ta.add(tvaluelist.get(i));
                 contents.put(aprefix+sep+tkeylist.get(i),ta);
               }          
	        }
	      }
	    
	    if (q==0)  // append to context keys
	    for (String key:contents.keySet()) 
	     if (key.indexOf(structure[0][0][0])>=0) contextkeys.add(key);
	    
	    }
	   
	   for ( String key : context.keySet()) 
         if (!key.equals("selfid"))
            contents.remove(key);
       
       
	   return contents;
	}

 
 
 
 
  int getint (HashMap<String,ArrayList<String>> h, String key) {
 
   return Integer.parseInt(h.get(key).get(0));
 
  }
  
  
  ArrayList<Integer> getintarray (HashMap<String,ArrayList<String>> h, String key) {
   
   ArrayList<Integer> a = new ArrayList <Integer> ();
   for (String s:h.get(key)) 
    a.add(Integer.parseInt(s));
    
   return a;
   
  }
  
  
  
  ArrayList <ArrayList <Integer>> get2ddotintarray (HashMap<String,ArrayList<String>> h, String key) {
 
    ArrayList <ArrayList <Integer>> s2darr = new ArrayList <ArrayList <Integer>> ();
    ArrayList <String> raw = h.get(key);
    for (String s: raw) {
    ArrayList <Integer> tarr = new ArrayList <Integer>();
    String[] sarr=s.split("\\.");
    for (int i=0;i<sarr.length;i++) tarr.add(Integer.parseInt(sarr[i]));
    s2darr.add(tarr);
    }
   return s2darr;
  } 
  


  void exitstatus(String status) {
   if (status.length()>0) {
    p(status);
    throw new RuntimeException(status);
   }
  }
 
  
 
 void initsequence (HashMap<String,ArrayList<String>> h,HashMap<String,ArrayList<String>> ratings, item myitem) {
 
  String status="";
  int minelem=0;
  int maxelem=0;
  int nchosensofar=0;
  
  Random generator = new Random();
  int k=generator.nextInt(10);
  for (int i=0; i<k; i++) {
    int j = generator.nextInt(10);
   }
   String mapid=h.get("selfid").get(0);
   
  // p(mapid); 
    
   //determine size of element array
   int nelem=getint(h,mapid+".all.nelem");
   if (nelem<=0) status+="there must be atleast one element\n"; exitstatus(status);
      
  // p(" nelem = "+Integer.toString(nelem));
   
   // determine musthaves
   ArrayList<Integer> musthave = getintarray(h,mapid+".all.musthave");
   // p(" musthave = "+musthave.toString()); 
  
   // determine groups 
   ArrayList <ArrayList<Integer>> group = get2ddotintarray(h,mapid+".all.group");
   if (group.get(0).get(0)==0) {
     ArrayList<Integer> tglist = new ArrayList<Integer> ();
     for (int i=0;i<nelem;i++) tglist.add(i+1);
     group.set(0,tglist);  
   }
   
   int ngroup = group.size();
   if (group.size()>nelem) status+="Number of groups cannot exceed nelem\n"; exitstatus(status);
   
   
   //p("group2d = "+group.toString());
   // groupindex, 1..nelem
   
    ArrayList<Integer> groupindex = new ArrayList<Integer> () ;
    groupindex.add(-1); //zero padding
    for (int i=1;i<=nelem;i++) groupindex.add(1);
    
    for (int i=0;i<ngroup;i++) {
     ArrayList<Integer> g = group.get(i);
     for (int j: g) 
      groupindex.set(j,i+1);  
    }
   
  // p("groupindex = "+groupindex.toString());
   
   // determine group minmaxes 
   ArrayList <ArrayList<Integer>> gminmax = get2ddotintarray(h,mapid+".all.gminmax");
   if (gminmax.size()!=ngroup) status+="length of gminmax should equal ngroup\n"; exitstatus(status);
   
   for (int i=0;i<ngroup;i++) {
    ArrayList<Integer> mm = gminmax.get(i);
    if (mm.size()!=2) status+="gminmax element should have 2 elements\n"; exitstatus(status);
    minelem=mm.get(0);maxelem=mm.get(1);
    if (minelem>maxelem) status+="minelem cant be greater than maxelem\n";exitstatus(status);
    if ((minelem<0) || (maxelem>group.get(i).size())) status+="gminmax range is invalid"; exitstatus(status);
   }
   
  // p ("gminmax = "+gminmax.toString());
   
   // determine gplace 
   ArrayList <ArrayList<Integer>> gplace = get2ddotintarray(h,mapid+".all.gplace");
 //  if (gplace.size()!=nelem) status+="length of gplace should equal nelem\n"; exitstatus(status);
  
    
   //p ("gplace = "+gplace.toString());
  
  // for (int i=0;i<ngroup;i++) {
   // ArrayList<Integer> pl = gplace.get(i);
    //for (int j:pl) 
     //if ((j<1) || (j>ngroup)) status+="gplace category id is invalid\n"; exitstatus(status);
   //}
   
   // determine gorder
   ArrayList<Integer> gorder = getintarray(h,mapid+".all.gorder");
   if (gorder.size()!=ngroup) status+="length of gorder should be ngroup\n"; exitstatus(status);
  
  // p ("gorder = "+gorder.toString());
  
  
   
   // determine gsequence
   ArrayList<String> gsequence = h.get(mapid+".all.gsequence");
   if (gsequence.size()!=ngroup) status+="length of gsequence should be ngroup\n"; exitstatus(status);
   
    //p ("gsequence = "+gsequence.toString());
  
   // initialize selected 1..nelem
   ArrayList<Integer> selected = new ArrayList<Integer> ();
   selected.add(-1); //zero padding 
   for (int i=1;i<=nelem;i++) selected.add(0); 
   
   //change the selected status of musthaves
   for (int i: musthave) 
    if (i>0) selected.set(i,1);   //tag the musthave elements as chosen using replace
   
    //p("selected = "+selected.toString());
  
   
   //make an optional which is the complement of musthave; this can have any number of elements
    ArrayList<Integer> optional = new ArrayList<Integer> ();
    for (int i=1;i<=nelem;i++) 
    if (selected.get(i)==0) optional.add(i);
    
    //p ("optional = "+optional.toString());
    
    ArrayList<Integer> nchosen = new ArrayList<Integer> ();
    nchosen.add(-1);
    for (int i=1;i<=ngroup;i++) nchosen.add(0);
    for (int i: musthave) 
     if (i>0) nchosen.set(groupindex.get(i),nchosen.get(groupindex.get(i))+1);
   
    //p ("nchosen = "+nchosen.toString());
    
    for (int i=1;i<=ngroup;i++)
     if (nchosen.get(i) > gminmax.get(i-1).get(0)) {status+="minimum is less than the musthave items for group";exitstatus(status);}
   
    
    //now select candidates for each group
    
    //lets shuffle each group
    
    for (ArrayList <Integer> a: group) Collections.shuffle(a);
    
     //p("group2d shuffled = "+group.toString());
    
    //pick elements from each group so that the total value is in the range minvalue to maxvalue
    
     for (int i=0;i<ngroup;i++) {
      ArrayList<Integer> g = group.get(i);
      nchosensofar=nchosen.get(i+1);
      minelem=gminmax.get(i).get(0);
      maxelem=gminmax.get(i).get(1);
      int ntarget=minelem+generator.nextInt(maxelem-minelem+1);
      //p("ntarget = "+ntarget+" chosensofar = "+nchosensofar);
      for (int j: g) { 
       int element= j;
       //p("element = "+element);
       if ((selected.get(element)==0) && nchosensofar<ntarget) {
        selected.set(element,1);
        nchosensofar++;
       }
      }
     }
   
    //p("selected after selection = "+selected.toString());
    
       
    // Now the sequencing begins
    
    //enforce gsequence for each group
    
    ArrayList<ArrayList<Integer>> sequence = new ArrayList<ArrayList<Integer>> ();
    for (int i=0;i<=ngroup;i++) sequence.add(new ArrayList<Integer>());
    
    for (int e=1;e<=nelem;e++) 
      if (selected.get(e)==1) 
		sequence.get(groupindex.get(e)).add(e);
     
    //p("sequence is = "+sequence.toString()); 
     //  p("gsequence is = "+gsequence.toString()); 
    
    //now apply gsequence directives
    
    for (int i=0; i<ngroup;i++) {
		if (gsequence.get(i).equals("random")) Collections.shuffle(sequence.get(i+1));
		if (gsequence.get(i).equals("reverse")) Collections.reverse(sequence.get(i+1));
    }
  //      p("sequence is = "+sequence.toString()); 

   // order directives.. sort nonzero groups.. then insert the zero groups randomly
   
   ArrayList<index> seqindex = new ArrayList<index> ();
   ArrayList<index> dontindex = new ArrayList<index> ();
   
   for (int i=0; i<ngroup; i++) {
   index tindex = new index();
    tindex.place=i;
    tindex.value=gorder.get(i); 
   if (gorder.get(i)!=0)  
     seqindex.add(tindex);
    else {
    tindex.value=0;
    dontindex.add(tindex);
    }
   }
  
  Collections.sort(seqindex);
  
  //now we add dontindex elements at random positions in seqindex
  
  for (index e:dontindex) {
    
    int position = generator.nextInt(seqindex.size()+1);
    seqindex.add(position,e);
  
  }
  
  
  //p("seqindex final");
  //for (index e: seqindex)
   //  System.out.print(e.place+" ");
  
  //p("");
  
  
  
  // so now we resequence in order..
  
  ArrayList <Integer> newsequence= new ArrayList <Integer> ();
  newsequence.add(-1);
  for (int i=0; i<ngroup; i++) {
    int g=seqindex.get(i).place;
    newsequence.addAll(sequence.get(g+1));
   }
   
  //p("newsequence = "+newsequence.toString());
  
  
  //finally use place.. the idea here is to preserve the
  //newsequence if it is consistent with the place
  //else look for something thats compatible
  //if not take what is available (instead of making exception)
  //and the results wont fully obey place constraints 
  
  int[] used = new int[nelem+1];
  used[0]=-1;
  for (int i=1;i<=nelem;i++) used[i]=-1;  // none are eligible
  
  for (int i=1;i<newsequence.size();i++) used[newsequence.get(i)]=0;  // these are eligible
  
  ArrayList <Integer> placesequence = new ArrayList <Integer> ();
  
  
  //p(gplace.toString());
  
  //p("");
 // p("groupindex="+groupindex.toString());
 // p("newsequence = "+newsequence.toString());
  
  // scope for simplifying the logic inside the loop
    
  //do this only if gplace size is more than 1.. so a 1-element 0 array will be ignored
  
  if (gplace.size()>1) {
  
       placesequence.add(-1);
 
  for (int i=1;(i<newsequence.size() && i<=gplace.size());i++) { //gplace has eligible categories for each spot
       boolean done=false;
       // check if current assignment is ok
        ArrayList<Integer> egroups = gplace.get(i-1);
       int current=groupindex.get(newsequence.get(i));
       //  p("used ="+used.toString());
       if ((egroups.contains(current) && used[newsequence.get(i)]==0)) {
         // p("i ="+i+"first loop egroups = "+egroups.toString()+"  current = "+current);
         used[newsequence.get(i)]=1;     
         placesequence.add(newsequence.get(i));
       } else
       //search in the newsequence to see if anything can be found for this spot
       {
         for (int s=1;s<newsequence.size();s++) {
          current=groupindex.get(newsequence.get(s));
          if ((egroups.contains(current) && used[newsequence.get(s)]==0)) {
        //  p("i = "+i+"second loop egroups = "+egroups.toString()+"  current = "+current);
          used[newsequence.get(s)]=1;     
          placesequence.add(newsequence.get(s));
          break; //nasty bug without break
        }
       } // s
      }  // else
     }  // for
    
   // if there are any selected items left out, they will be added at the end
   // this should happen only due to specification errors
   
         for (int s=1;s<newsequence.size();s++) 
          if (used[newsequence.get(s)]==0) {
          used[newsequence.get(s)]=1;     
          placesequence.add(newsequence.get(s));
       } // s
    
    }
    
    else
    
     {
        placesequence.addAll(newsequence);
     }
       
  // p("place sequence = "+placesequence.toString());
   
   ArrayList <String> ssequence = new ArrayList <String> ();
   
   for (int i=1;i<placesequence.size();i++) ssequence.add(placesequence.get(i).toString());
   
   //put things in map that will be actually used
   
   h.put(h.get("selfid").get(0)+".all.items",ssequence);
   
  String self=h.get("selfid").get(0);
 // p("self= "+self);
  
  
  if (myitem!=null) {
  
  myitem.nitems+=ssequence.size();
  
  for (int i=0;i<ssequence.size();i++) {
   
     String sid=h.get(self+".rowid."+ssequence.get(i)).get(0);
     
     String hid=h.get("selfid").get(0);
     
     myitem.id.add(sid);
     
     if (h.get(hid+".all.prefix")==null) myitem.prefix.add(""); else myitem.prefix.add(h.get(hid+".all.prefix").get(0));
     
     if (h.get(hid+".all.precheck")==null) myitem.precheck.add(""); else myitem.precheck.add(h.get(hid+".all.precheck").get(0));
     
     
     if (h.get(sid+".stem")!=null) myitem.stem.add(h.get(sid+".stem").get(0));
       else {
     
     p("sid= "+sid+"  hid= "+hid+"  i= "+i);   
    }
     
    // p(i+ " sid = "+sid);
     String rid ="";
     
     if (h.get(sid+".choices")!=null) rid=h.get(sid+".choices").get(0);else rid=h.get(self+".all.choices").get(0);
     
   // p(i+" rid = " +rid);
   
     // if (h.get(sid+".precheck")!=null) rid=h.get(sid+".precheck").get(0);else rid=h.get(self+".all.precheck").get(0);
     
     
     String minsel="1"; String maxsel="1";
     if (ratings.get("ratings.minsel")!=null) minsel= ratings.get("ratings.minsel").get(0);
     if (ratings.get("ratings.maxsel")!=null) maxsel= ratings.get("ratings.maxsel").get(0);
     
     if (ratings.get("ratings."+rid+".minsel")!=null) minsel= ratings.get("ratings."+rid+".minsel").get(0);
     if (ratings.get("ratings."+rid+".maxsel")!=null) maxsel= ratings.get("ratings."+rid+".maxsel").get(0);
        
     
     myitem.minselect.add(minsel);
     myitem.maxselect.add(maxsel);
     
     
     
     int noptions=ratings.get("ratings."+rid+".options").size();
     int max=0;
     
     for (int j=0;j<noptions;j++) 
       if (ratings.get("ratings."+rid+".options").get(j).length() > max) max=ratings.get("ratings."+rid+".options").get(j).length();
     
     myitem.bwidth.add(Integer.toString(max));
     
     // the key aspects are to reverse the options list and values list UNLESS the direction is left 
     
     boolean right=true;
     
     if (ratings.get("ratings.all.direction").get(0).equals("right")) right=true; else right=false;
     if (ratings.get("ratings."+rid+".direction")!=null) {
       if (ratings.get("ratings."+rid+".direction").get(0).equals("left")) right=false;else right=true;
     } 
    
    ArrayList <String> tolist = new ArrayList <String> ();
    tolist.addAll(ratings.get("ratings."+rid+".options")); 
     
     if (right) Collections.reverse(tolist); 
     myitem.options.add(tolist);
     
    ArrayList<String> tvals = new ArrayList<String> (); 
    for (int j=0;j<noptions;j++) 
     {
     if (right)
       tvals.add(Integer.toString(noptions-j));
     else tvals.add(Integer.toString(j+1));
    }
     
    
     myitem.values.add(tvals); 
     
     //reset flow and ncols if required
     
     String flow=ratings.get("ratings.all.flow").get(0);
     if (ratings.get("ratings."+rid+".flow")!=null) 
        flow=ratings.get("ratings."+rid+".flow").get(0);
     myitem.flow.add(flow);
     
     String ncols=ratings.get("ratings.all.ncols").get(0);
     if (ratings.get("ratings."+rid+".ncols")!=null) 
         ncols=ratings.get("ratings."+rid+".ncols").get(0);
     myitem.ncols.add(ncols);
     
     
    } 
  }
  
 }  
 
%>


<% 
HashMap<String,ArrayList<String>> emptymap = new HashMap<String,ArrayList<String>> ();
	insertmapvalue(emptymap,"selfid","empty");
	HashMap<String,ArrayList<String>> ratingsmap = map2d(emptymap, ratings);
    item it = new item();
    for (int j=0; j < nscales;j++) {
	    HashMap<String,ArrayList<String>> h = new HashMap<String,ArrayList<String>> () ;
	     h = map2d(emptymap,scales.get(j));
	     initsequence(h,ratingsmap,it);
	   }
%>



<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" >
<head>
<script language="JavaScript" type="text/javascript" src="/implicit/common/en-us/js/task.js"></script>
    <meta http-equiv="msthemecompatible" content="no">
    <style type="text/css">
       body {font-family:arial;}
      .big {font-size: 24px; line-height:150%}
      .med {font-size: 16px;}
       table.grid {font-size: 16px;font-weight:bold;} 
      .sml {font-size: 12px;}
    </style>
    <title>Scenarios</title>
    
<script language="JavaScript">

//window.moveTo(50,50);
//window.resizeTo(screen.width-100,screen.height-100);
 var xmlhttp=false;
 /*@cc_on @*/
 /*@if (@_jscript_version >= 5)
// JScript gives us Conditional compilation, we can cope with old IE versions.
// and security blocked creation of the objects.
 try {
  xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
 } catch (e) {
  try {
   xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
  } catch (E) {
   xmlhttp = false;
  }
 }
@end @*/
if (!xmlhttp && typeof XMLHttpRequest!='undefined') {
  xmlhttp = new XMLHttpRequest();
}


 function dummyjsp() {
    xmlhttp.open("GET", "/implicit/common/en-us/html/blank.jsp",true);
    xmlhttp.send("");
   }   
  
var spaces4 = '&nbsp;&nbsp;&nbsp;&nbsp;';
var spaces8 = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
var spaces16 = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';


var begintaskrt=new Date().getTime();
  

 function Begin() {
    setInterval("dummyjsp()", 300000);
    mainitem.style.display='block';
    js_startrt[0] = new Date().getTime();
    mainitem.innerHTML=makeItem(0);
   }
   
   function Instructions() {
    mainitem.style.display='block';
    mainitem.innerHTML="<br/><br/><table align=center width=70%><tr><td class=big><%=instructions%><br/><br/><center><button type=button style='font-size:24' onclick='Begin()'>Click to Start </button></center></td></tr></table>";
   }
   

</script>  


</head>

<body onLoad='Instructions();'>

 
 <form method="post" action="/implicit/Study" name="form1" onSubmit="return assignformvalues();">
 <input type="hidden" name="mode" value="insQuesData">
 <input type="hidden" name="mycondition" value="<%=mycondition%>">
 
 

<div id='mainitem' style='display:none'></div>

<div id='mybutton' style='display:none'>
<br /><br />
<center> 
<script language="JavaScript" type="text/javascript">writeButton("CONTINUE TO NEXT PAGE");</script> 
</center>
</div>

<%     
    for (int i=0;i<it.nitems;i++) 
     { String id=it.id.get(i);  id = id.substring(id.indexOf('.')+1,id.length());
  %>
  <input type="hidden" name="<%=id%>" value=".">
  <input type="hidden" name="rt<%=id%>" value="0">
  <input type="hidden" name="trt<%=id%>" value="0">
  <input type="hidden" name="atm<%=id%>" value="0">
  
 <% } %> 

 
 <script language="JavaScript">

   var js_mycondition = <%=mycondition%>;
  var js_nitem = <%=it.nitems%>;
  var js_stem = new Array(js_nitem);
  var js_prefix = new Array(js_nitem);
  var js_precheck = new Array(js_nitem);
  
  
  //initialize js_stems to hold all the stems
  <% 
  
   
  
  for (int i=0;i<it.nitems;i++) { 
   String  prefix= it.prefix.get(i);
   String stem = it.stem.get(i);
   String precheck=it.precheck.get(i); 
         %>
     js_stem[<%=i%>]="<%=stem%>";
     js_prefix[<%=i%>]="<%=prefix%>";
     js_precheck[<%=i%>]="<%=precheck%>";
     
     
   <% } %>  
  
  //initialize js_options to hold all the options
  
  var js_options = new Array(js_nitem);
  var js_values = new Array(js_nitem);
  var js_nbuttons = new Array(js_nitem);
  var js_ncols = new Array(js_nitem);
  var js_flow = new Array(js_nitem);
  var js_bwidths = new Array(js_nitem);
  var js_minselect = new Array(js_nitem);
  var js_maxselect = new Array(js_nitem);
  var js_startrt = new Array(js_nitem);
  var js_endrt = new Array(js_nitem);
  var js_varnames = new Array(js_nitem);
  var js_shortvarnames = new Array(js_nitem);
  var js_neganswers = new Array(js_nitem);
  var js_nattempts = new Array(js_nitem);
  
  
  
  
    

  <%  for (int i=0;i<it.nitems;i++) { 
     
    %>
     js_options[<%=i%>]= new Array( <%=it.options.get(i).size()%> );
     js_values[<%=i%>]= new Array( <%=it.options.get(i).size()%> );
   
     
     js_nbuttons[<%=i%>]= <%=it.options.get(i).size()%>;
     js_varnames[<%=i%>]= "<%=it.id.get(i)%>";
     js_shortvarnames[<%=i%>]= "<%=it.id.get(i).substring(it.id.get(i).indexOf('.')+1,it.id.get(i).length())%>";
    
     js_neganswers[<%=i%>] = <%=neganswers[i]%>;
      
     js_nattempts[<%=i%>] = 0;
      
     js_ncols[<%=i%>]= "<%=it.ncols.get(i)%>";
     js_flow[<%=i%>]= "<%=it.flow.get(i)%>";
     
     
     <% for (int j=0;j<it.options.get(i).size();j++) { %>
        js_options[<%=i%>][<%=j%>]="<%=it.options.get(i).get(j)%>";
      <% } %>  
     <% for (int j=0;j<it.options.get(i).size();j++) { %>
        js_values[<%=i%>][<%=j%>]="<%=it.values.get(i).get(j)%>";
      <% } %>  
     js_bwidths[<%=i%>]= 90 + ( <%=it.bwidth.get(i)%> - 8)*8;
     if (js_bwidths[<%=i%>] > 900) js_bwidths[<%=i%>] = 900;
     js_minselect[<%=i%>]=<%=it.minselect.get(i)%>;
     js_maxselect[<%=i%>]=<%=it.maxselect.get(i)%>;
        
   <% } %>  
   
  var js_selected = new Array(js_nitem);
  for (i=0;i<js_nitem;i++) js_selected[i]=-900;
 
  var js_multibuttons = new Array(101);
  for (i=0;i<101;i++) js_multibuttons[i]=0;
 
  
</script>



<script language="JavaScript">
  
  var mainitem = document.getElementById('mainitem');
  var mybutton = document.getElementById('mybutton');
  
  var gnext="";

 


function assignformvalues () {
  
   mainitem.innerHTML='...';

  <% for (int i=0;i<it.nitems;i++) {
  String id=it.id.get(i);
   id = id.substring(id.indexOf('.')+1,id.length());
   %>
  document.form1.rt<%=id%>.value = (js_endrt[<%=i%>] - js_startrt[<%=i%>]).toString();
  document.form1.trt<%=id%>.value = (js_endrt[<%=i%>] -  begintaskrt).toString();
  document.form1.atm<%=id%>.value =  js_nattempts[<%=i%>].toString();
  
  if (js_maxselect[<%=i%>]<=1) {
  if (js_selected[<%=i%>]!=-999) document.form1.<%=id%>.value = js_selected[<%=i%>].toString();
    else document.form1.<%=id%>.value = '.';
    } else {
    
    if (js_selected[<%=i%>]!=-999) document.form1.<%=id%>.value = js_selected[<%=i%>];
      else document.form1.<%=id%>.value = '.';
       
    }
 <% } %>
	
  return true;
}



// #BDEDFF light blue
   
 var globalcuritem=-1;
 var globalfib='';
 
 var globalitemtype=-1; 
 var correctanswer=true;
  
 function makeItem(curitem) {
 
 var nowtime = new Date().getTime();
 
  
  
 
     
  correctanswer=true;
   
  var maxselect = js_maxselect[curitem];
  
  var icols=js_ncols[curitem];
  var nbuttons=js_nbuttons[curitem];
  var buttonwidth=js_bwidths[curitem];
  var colwidth=buttonwidth+20;
  var prefix = '<button type="button" style="font-size:16; font-weight: bold;text-align:left; width:'+buttonwidth+'px; background-color:';
  var prefixskip = '<button type="button" style="font-size:10; font-weight: bold; width: 50px; background-color:';
  var curdisplay= '<table width="80%" align=center><tr><td><font color="#AAAAAA"><b></b></font><br /> <span  class=med>'+js_prefix[curitem]+'</span><br/><span class=big style="background-color: #FFFFDD">';
  
  globalcuritem=curitem;
  globalitemtype = curitem % 2;
  
  if ((curitem % 2 == 0) && (js_mycondition!=2))  {
  
     globalfib=''; var mystem=''; k=0;
     
     for (i=0;i<js_stem[curitem].length;i++) 
       if (js_stem[curitem].charAt(i)=='[') {
          globalfib+=js_stem[curitem].charAt(i+1); k++;
          mystem+= '<input type=text style="font-family:arial;font-size:24;width:24px;text-align:center"  maxlength=1 id=blank'+k+' name=blank'+k+' onkeypress="keyCode=(event.which)?event.which:event.keyCode;return (keyCode != 13); " onkeydown="keyCode=(event.which)?event.which:event.keyCode;return (keyCode != 13); ">';
          i++;i++;
         } else if (i<js_stem[curitem].length) mystem+=js_stem[curitem].charAt(i);
    
     curdisplay+=mystem +' </span><br/><br/>';
   
   curdisplay+='<button type="button" id="thisbut" style="font-size:16; font-weight: bold; width: 100px; background-color:#EEEEEE" onmouseup=\'checkfill("thisbut")\'>Next</button></td></tr></table>';     
  
  } else
    
     {
     
     curdisplay+=js_stem[curitem]+'</span><br/><br/></td></tr></table> <table width="80%" align=center cellpadding="2">';
  
  var cols=0;
  
  curdisplay+='<tr>';
  for (var i=0; i < nbuttons; i++) {
      cols=cols+1;
      curdisplay+='<td class="med" width="'+colwidth+'px">'+prefix;
     
     // single select case
      if (maxselect==1) {
      
      if (js_selected[curitem]>0) {
      if ((js_shortvarnames[curitem].charAt(0)=="p") && (js_selected[curitem]==js_neganswers[curitem])) correctanswer=false;
      if ((js_shortvarnames[curitem].charAt(0)=="n") && (js_selected[curitem]!=js_neganswers[curitem])) correctanswer=false;
     }
      
      if ((js_values[curitem][i]==js_selected[curitem]) && correctanswer)
       
            curdisplay+='yellow"'; 
       
      else curdisplay+='#EEEEEE"';
     
      if ((js_values[curitem][i]==js_selected[curitem]) && correctanswer)                             
          curdisplay+=' onmouseup=\'nextItem('+(curitem+1)+','+curitem+','+js_values[curitem][i]+')\'';
       else curdisplay+=' onmouseup=\'nextItem('+curitem+','+curitem+','+js_values[curitem][i]+')\'';
      
      curdisplay+='>'+js_options[curitem][i]+'</button></td>'; 
      } // maxselect is 1
      
     
      // multiple select case
      if (maxselect>1) {
      
      if (js_multibuttons[i]==1) 
          curdisplay+='#BDEDFF"';
      else curdisplay+='#EEEEEE"';
     
      curdisplay+=' onmouseup=\'nextItem('+curitem+','+curitem+','+i+')\'';
      
      curdisplay+='>'+js_options[curitem][i]+'</button></td>'; 
      } // maxselect is > 1
      
      
     
      if (cols==icols) {
       curdisplay+= '</tr>';
       if (i<nbuttons-1) curdisplay+='<tr>';
       cols=0;
      }
   }
    
   }

  // the finished button
  
   if (maxselect>1) {
          curdisplay+='<tr><td class="med"><br/><br/>'+prefixskip;
  
          if (js_multibuttons[100]==1) 
              curdisplay+='yellow"';
            else curdisplay+='#EEEEEE"';
            
          if (js_multibuttons[100]==1)                             
              curdisplay+=' onmouseup=\'nextItem('+(curitem+1)+','+curitem+',100)\'';
		  else curdisplay+=' onmouseup=\'nextItem('+curitem+','+curitem+',100)\'';
          
          curdisplay+='>Finished and continue</button></td></tr>';
         
    curdisplay+='<tr><td colspan="'+icols+'">Select as many items as you wish, turning them <u>BLUE</u>.<br/>To remove an item from selection, click it once more.<br>When you are done select the &ldquo;Finished and continue&rdquo; button.<br/>Once the Finished button turns <u>YELLOW</u>, you can confirm by clicking it a second time.<br/><br/></td></tr>';
}
   
  
   if ((curitem==-1)) curdisplay+='<tr><td class=sml colspan="'+icols+'"><br/><br/>Selecting an answer once colors it yellow.<br/> You can change your answer by selecting another option. <br /> To confirm, click the selected (yellow) button a second time.<br/><br/></td></tr>';

          
          curdisplay+='</table><table width="20%" align=right>';

       
         if (curitem<0) {
          curdisplay+='<tr><td align="center" class="med"><br/><br/>'+prefixskip;
         
         if (maxselect<=1) {
          if (js_selected[curitem]==-999) 
              curdisplay+='yellow"';
            else curdisplay+='#EEEEEE"';
          if (js_selected[curitem]==-999)                             
              curdisplay+=' onmouseup=\'nextItem('+(curitem+1)+','+curitem+',-999)\'';
		  else curdisplay+=' onmouseup=\'nextItem('+curitem+','+curitem+',-999)\'';
          }
          
         
         if (maxselect>1) {
          if (js_multibuttons[99]==1) 
              curdisplay+='yellow"';
            else curdisplay+='#EEEEEE"';
          if (js_multibuttons[99]==1)                             
              curdisplay+=' onmouseup=\'nextItem('+(curitem+1)+','+curitem+',99)\'';
		  else curdisplay+=' onmouseup=\'nextItem('+curitem+','+curitem+',99)\'';
          }
          
        
          
          curdisplay+='>Skip</button></td></tr>';
          
         }  
      
   curdisplay+='</table><br/><br/><br/><br/>';
   return(curdisplay);
 }
   
   
 
 
 function checkfill(selfid) {
 
 /// alert(cur+" "+fieldid);
 
  cur=globalcuritem;
  curfib=globalfib;
  js_nattempts[cur]++;
  
  z=curfib.length;
  
  if (z>=1)   typedsofar=document.getElementById("blank1").value;
  if (z>=2)   typedsofar+=document.getElementById("blank2").value;
  if (z==3)   typedsofar+=document.getElementById("blank3").value;
  
  typedsofar = typedsofar.toLowerCase();
  curfib=curfib.toLowerCase();
  
  if (typedsofar==curfib || js_nattempts[cur]>=3) {
  
  document.getElementById(selfid).style.backgroundColor="yellow";
  if (typedsofar==curfib) js_values[cur][0] =  1; else js_values[cur][0] =  0;
  if (js_nattempts[cur]>=3) nextItem(cur+1,cur,js_values[cur][0]);
  else document.getElementById(selfid).onmouseup= function() {nextItem(cur+1,cur,js_values[cur][0]);};
 } else alert('Please type in the correct letter(s) and then click "Next".');
 
 }
 
 
 
 function nextItem(next,prev,button) {

   var maxselect = js_maxselect[prev];
   var nbuttons=   js_nbuttons[prev];
   
   if (maxselect<=1) js_selected[prev]=button;
    else {
     if (button>=0) js_multibuttons[button]= 1 - js_multibuttons[button]; // toggle state 
     if (button!=100) js_multibuttons[100]=0;
     if (button!=99) js_multibuttons[99]=0;
     
          
    }
  
   if (next!=js_nitem) {
   if (next!=prev) mainitem.innerHTML='';
   if (next!=prev) js_endrt[prev]= new Date().getTime();
   if (next!=prev) {
     
     //now we need to save
     
     var tcounter=0;
     
     if (maxselect>1) {
     sres="";
     for (i=0;i<nbuttons;i++)
      if (js_multibuttons[i]==1) { 
       tcounter++;
       if (tcounter>1) sres+="*"+js_values[prev][i];
        else sres+=js_values[prev][i];
       }
       
     //  sres+=js_values[prev][nbuttons-1];
       
     if (sres=="") js_selected[prev]="-999";
      else js_selected[prev]=sres;
      //alert(js_selected[prev]);
      for (i=0;i<101;i++) js_multibuttons[i]=0;
     }
     
     js_startrt[next] = new Date().getTime();
   }
   gnext=next;
 //  if (next!=prev) setTimeout("mainitem.innerHTML=makeItem(gnext);",25);
 if (next!=prev) mainitem.innerHTML=makeItem(gnext);
     else mainitem.innerHTML=makeItem(gnext);
     if (globalitemtype==1)  js_nattempts[prev]++;
     if ((!correctanswer) &&  (globalitemtype==1) ) alert("Incorrect. Please use information from the scenario to answer the question correctly.");
    }
    
    else {js_endrt[prev]= new Date().getTime();
    // window.location="/implicit/user/sriram/reimer/index.htm";
    setTimeout('mainitem.style.display="none";check=assignformvalues(); document.form1.submit(); ', 25);
      }     
   }

</script>


<script language="JavaScript">
var message="Context menu is disabled.";

function clickIE4(){
if (event.button==2){
alert(message);
return false;
}
}

function clickNS4(e){
if (document.layers||document.getElementById&&!document.all){
if (e.which==2||e.which==3){
alert(message);
return false;
}
}
}

if (document.layers){
document.captureEvents(Event.MOUSEDOWN);
document.onmousedown=clickNS4;
}
else if (document.all&&!document.getElementById){
document.onmousedown=clickIE4;
}

document.oncontextmenu=new Function("alert(message);return false")
</script>
</form>



</body>
</html>

 
