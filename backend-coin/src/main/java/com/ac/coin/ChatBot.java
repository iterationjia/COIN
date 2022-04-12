package com.ac.coin;

import com.ac.coin.enums.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatBot {
    public static String stock="unknown";
    public static String secondIndustry="unknown";
    public static String askedQuestion="unknown";

    public static String[] question_analysis(String question) throws IOException, InterruptedException {
        System.out.println('#');
        System.out.println(askedQuestion.length());

        String[] questions=question.split("的|在");
        if(!questions[0].contains("他") && !questions[0].contains("她") && !questions[0].contains("它")) askedQuestion=questions[0];
        else questions[0] = askedQuestion+"的";

        System.out.println(questions[0]+questions[1]);

        String temp_question= "";
        String temp_answer="";
        if (questions.length>2){
            temp_question= askedQuestion + questions[1];
            temp_answer=QA_machine(temp_question);
            System.out.println("temp_question:"+temp_question);
            System.out.println("temp_answer:"+temp_answer);

            for(int i=2;i<questions.length;i++){
                temp_question=temp_answer+questions[i];
                temp_answer=QA_machine(temp_question);
                System.out.println("temp_question:"+temp_question);
                System.out.println("temp_answer:"+temp_answer);
            }
        }else temp_answer=QA_machine(questions[0]+questions[1]);
        String[] answers=new String[3];
        answers[0]=temp_answer;
        answers[1]=secondIndustry;
        answers[2]=stock;
        return answers;
    }
    public static String QA_machine(String question) throws IOException, InterruptedException {
        Process proc;
        String pythonPATH = Path.pythonPath.getValue();
        String chatBotPATH = "src\\main\\python\\chatRobot\\chatbot_graph.py";
        String[] args1 = new String[] {pythonPATH,chatBotPATH,question};
        proc = Runtime.getRuntime().exec(args1);
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        System.out.println(in.readLine());
        String line = null;
        String answer="";
        int flag=0;
        int a_flag=0;
        while ((line = in.readLine()) != null) {
            if (line.equals("STOCK")) {
                stock = in.readLine();
                flag = 1;
                a_flag=1;
            }
            else if (line.equals("result")){
                a_flag=1;
                secondIndustry=in.readLine();
                if (flag!=1){
                    stock=in.readLine();
                }
            }else {
                System.out.println(line);
                answer += line + " ";
            }
        }
        if (a_flag==0){
            stock="unknown";
            secondIndustry="unknown";
        }
        in.close();
        return answer;
    }
}