using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using LayerDAL;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;


namespace LayerBLL.Logics
{
    public class ExercicioLogic
    {
        public static async Task<Response> GetExerciciosLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Exercicio> exercicioList = await LayerDAL.ExercicioService.GetExerciciosService(sqlDataSource);
            if (exercicioList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Sucesso na obtenção dos dados";
                response.Data = new JsonResult(exercicioList);
            }
            return response;
        }
    }
}
