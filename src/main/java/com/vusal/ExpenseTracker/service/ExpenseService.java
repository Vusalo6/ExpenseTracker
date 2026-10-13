package com.vusal.ExpenseTracker.service;
import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.Entity.Expense;
import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.dto.ExpenseRequestDto;
import com.vusal.ExpenseTracker.dto.ExpenseUpdateDto;
import com.vusal.ExpenseTracker.exception.ResourceNotFoundException;
import com.vusal.ExpenseTracker.repos.CategoryRepo;
import com.vusal.ExpenseTracker.repos.ExpenseRepo;
import com.vusal.ExpenseTracker.repos.UserRepo;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class ExpenseService { //Dependency injection in Constructions
    private final ExpenseRepo expenseRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;


    public ExpenseService(ExpenseRepo expenseRepo,UserRepo userRepo,CategoryRepo categoryRepo){
        this.expenseRepo=expenseRepo;
        this.userRepo=userRepo;
        this.categoryRepo=categoryRepo;
    }
    public Expense getExpense(Long id){
       return expenseRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Expense with this iD doesnt exist"));

    }
    public BigDecimal getTotal(List<Expense> expenses){
        BigDecimal total = BigDecimal.ZERO;

        for(Expense e:expenses){
            total =  total.add(e.getAmount());
        }
        return total;
    }

    public void createExpense(ExpenseRequestDto expense){
        Expense newExpense= new Expense();
        newExpense.setUser(
                findUserById(expense.getUserId())
        );

        newExpense.setCategory(
                findCategoryById(expense.getCategoryId())
        );

        newExpense.setDescription(expense.getDescription());
        newExpense.setDate(expense.getDate());
        newExpense.setAmount(expense.getAmount());

        expenseRepo.save(newExpense);
    }
    public void updateExpense(ExpenseUpdateDto expense, Long id){
        Expense existingExpense = expenseRepo.findById(id).orElseThrow();
        Category category = findCategoryById(expense.getCategoryId());
        existingExpense.setCategory(category);
        existingExpense.setAmount(expense.getAmount());
        existingExpense.setDescription(expense.getDescription());
        existingExpense.setDate(expense.getDate());
        expenseRepo.save(existingExpense);
    }
    public void deleteExpense(Long id){
        expenseRepo.deleteById(id);
    }
    public List<Expense> findByUser(User user){
        return expenseRepo.findByUser(user);
    }
    public List<Expense> findByUserAndCategory(User user, Category category){
        return expenseRepo.findByUserAndCategory(user,category);

    }
    public List<Expense> findByUserAndDate(User user, LocalDate date){
      return expenseRepo.findByUserAndDate(user, date);
    }
    public List<Expense> getByUserAndDatesBetween(User user,LocalDate fromDate,LocalDate toDate){
      long days = ChronoUnit.DAYS.between(fromDate,toDate);
      if (days<0){
          throw new IllegalArgumentException("from Date cannot be after toDate");
      }
      if (days>90) {
          throw new IllegalArgumentException("Date range cannot exceed 90 days");
      }
       return  expenseRepo.findByUserAndDateBetween(user, fromDate, toDate);

    }
    public List<Expense> getThisWeekExpenses(User user){
        LocalDate today =LocalDate.now();
        LocalDate monday= today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

      return   expenseRepo.findByUserAndDateBetween(user,monday,sunday);

    }
    public List<Expense> getLastWeekExpenses(User user){
        LocalDate today=LocalDate.now();
        LocalDate mondayOfLastWeek = today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        LocalDate sundayOfLastWeek  = mondayOfLastWeek.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        return expenseRepo.findByUserAndDateBetween(user,mondayOfLastWeek,sundayOfLastWeek);
    }

    public List<Expense> getThisMonthExpenses(User user){
        LocalDate today =LocalDate.now();
        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        return expenseRepo.findByUserAndDateBetween(user,firstDayOfMonth,lastDayOfMonth);
    }
    public List<Expense> getLastMonthExpenses(User user){ // how de we get user
        LocalDate today = LocalDate.now();
        LocalDate previousMonth = today.minusMonths(1);
        LocalDate firstDayOfLastMonth = previousMonth.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfLastMonth = previousMonth.with(TemporalAdjusters.lastDayOfMonth());
        return expenseRepo.findByUserAndDateBetween(user,firstDayOfLastMonth,lastDayOfLastMonth);
    }
    public List<Expense> findByUserAndCategoryAndDate(User user, Category category, LocalDate time){
        return expenseRepo.findByUserAndCategoryAndDate(user,category,time);
    }
    public Category findCategoryById(Long id){
        if (id == null) {
            return null;
        }
       return categoryRepo.findById(id).orElseThrow();
    }
    public User findUserById(Long id){
        return  userRepo.findById(id).orElseThrow(()->new  ResourceNotFoundException("Expense not found: " + id));
    }
}
